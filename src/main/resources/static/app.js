const STOMP_URL = 'wss://eshonkulov-asliddin.jprq.app/mu-autotest';
const SUBMIT_TASK_BUTTON_ID = 'submitTask';
const START_TASK_BUTTON_ID = 'startTask';
const SPINNER_DISPLAY_STYLE = 'inline-block';
const SPINNER_HIDE_STYLE = 'none';
const TASK_IN_PROGRESS_KEY = 'taskInProgress';
const LAB_ID_KEY = 'labId';
const CALL_BUILD_AND_TEST_REPO_KEY = 'callBuildAndTestRepository';

const stompClient = new StompJs.Client({
    brokerURL: STOMP_URL
});

function toggleSpinner(buttonId, spinnerId, action) {
    const button = document.getElementById(buttonId);
    const spinner = document.getElementById(spinnerId);
    button.disabled = action === 'show';
    spinner.style.display = action === 'show' ? SPINNER_DISPLAY_STYLE : SPINNER_HIDE_STYLE;
}

function showSpinner(buttonId, spinnerId) {
    toggleSpinner(buttonId, spinnerId, 'show');
}

function hideSpinner(buttonId, spinnerId) {
    toggleSpinner(buttonId, spinnerId, 'hide');
}

function hideButton(buttonId) {
    const button = document.getElementById(buttonId);
    button.style.display = 'none';
}

function reconnectIfNecessary() {
    const repoName = $(`#${SUBMIT_TASK_BUTTON_ID}`).data("repo-name");
    if (sessionStorage.getItem(TASK_IN_PROGRESS_KEY) === 'true') {
        showSpinner(SUBMIT_TASK_BUTTON_ID, `${SUBMIT_TASK_BUTTON_ID}Spinner`);
        stompClient.activate();
        stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);
            stompClient.subscribe(`/user/queue/attempts/${repoName}`, (response) => {
                console.log(JSON.parse(response.body));
                addAccordionItem(JSON.parse(response.body));
                disconnect();
                hideSpinner(SUBMIT_TASK_BUTTON_ID, `${SUBMIT_TASK_BUTTON_ID}Spinner`);
            });
        };
    }
}

window.addEventListener('load', reconnectIfNecessary);

function submitTask() {
    const labId = parseInt($(`#${SUBMIT_TASK_BUTTON_ID}`).data("lab-id"));
    const repoName = $(`#${SUBMIT_TASK_BUTTON_ID}`).data("repo-name");
    console.log("lab id", labId);
    showSpinner(SUBMIT_TASK_BUTTON_ID, `${SUBMIT_TASK_BUTTON_ID}Spinner`);
    sessionStorage.setItem(TASK_IN_PROGRESS_KEY, 'true');
    sessionStorage.setItem(LAB_ID_KEY, labId);

    stompClient.activate();
    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe(`/user/queue/attempts/${repoName}`, (response) => {
            const buildResult = JSON.parse(response.body);
            console.log(buildResult);

            if (buildResult.status === 'error') {
                alert(buildResult.message);
            }else {
                addAccordionItem(buildResult);
            }
            hideSpinner(SUBMIT_TASK_BUTTON_ID, `${SUBMIT_TASK_BUTTON_ID}Spinner`);
            sessionStorage.clear();
            disconnect();
        });
        buildAndTestRepository();
    };

    stompClient.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
        sessionStorage.clear();
        hideSpinner(SUBMIT_TASK_BUTTON_ID, `${SUBMIT_TASK_BUTTON_ID}Spinner`);
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
        sessionStorage.clear();
        hideSpinner(SUBMIT_TASK_BUTTON_ID, `${SUBMIT_TASK_BUTTON_ID}Spinner`);
    };
}

function startTask() {
    showSpinner(START_TASK_BUTTON_ID, `${START_TASK_BUTTON_ID}Spinner`);
    stompClient.activate();
    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/repositories', (response) => {
            const userTakenLab = JSON.parse(response.body);

            if (userTakenLab.status === 'error') {
                alert(userTakenLab.message);
                hideSpinner(START_TASK_BUTTON_ID, `${START_TASK_BUTTON_ID}Spinner`);
            } else {
                const { id, githubUrl } = userTakenLab;
                console.log("Started Lab: ", userTakenLab);

                hideSpinner(START_TASK_BUTTON_ID, `${START_TASK_BUTTON_ID}Spinner`);
                addRepositoryCard(githubUrl, id);
            }
            disconnect();
        });
        generateRepository();
    };

    stompClient.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
        sessionStorage.clear();
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
        sessionStorage.clear();
    };
}

function disconnect() {
    stompClient.deactivate();
    console.log("Disconnected");
}

function buildAndTestRepository() {
    if (stompClient.connected) {
        const labId = parseInt($(`#${SUBMIT_TASK_BUTTON_ID}`).data("lab-id"));
        console.log(labId);
        stompClient.publish({
            destination: "/app/submitTask",
            body: JSON.stringify({ 'labId': labId })
        });
    } else {
        console.error('WebSocket connection not established');
    }
}

function generateRepository() {
    if (stompClient.connected) {
        const labId = parseInt($(`#${START_TASK_BUTTON_ID}`).data("not-started-lab-id"));
        console.log(labId);
        stompClient.publish({
            destination: "/app/generateRepository",
            body: JSON.stringify({ 'labId': labId })
        });
    } else {
        console.error('WebSocket connection not established');
    }
}

function buildRepositoryCard(githubUrl) {
    const cardDiv = document.createElement("div");
    cardDiv.classList.add("card");

    const cardBodyDiv = document.createElement("div");
    cardBodyDiv.classList.add("card-body");

    const cardTitle = document.createElement("h5");
    cardTitle.classList.add("card-title");
    cardTitle.textContent = "GitHub Repository";

    const cardText = document.createElement("p");
    cardText.classList.add("card-text");
    cardText.textContent = "Check out task on GitHub:";

    const cardLink = document.createElement("a");
    cardLink.setAttribute("href", githubUrl);
    cardLink.setAttribute("target", "_blank");
    cardLink.classList.add("btn", "btn-primary");
    cardLink.textContent = "GitHub Repository";

    cardBodyDiv.append(cardTitle, cardText, cardLink);
    cardDiv.appendChild(cardBodyDiv);

    return cardDiv;
}

function createSubmitTaskButton(labId) {
    const button = document.createElement('button');
    button.id = SUBMIT_TASK_BUTTON_ID;
    button.className = 'btn btn-dark btn-block';
    button.dataset.labId = labId;

    const icon = document.createElement('i');
    icon.className = 'fab fa-github mr-1';

    const spinner = document.createElement('span');
    spinner.id = `${SUBMIT_TASK_BUTTON_ID}Spinner`;
    spinner.className = 'spinner-border spinner-border-sm';
    spinner.role = 'status';
    spinner.ariaHidden = 'true';
    spinner.style.display = 'none';

    button.append(icon, document.createTextNode(' Submit '), spinner);

    const div = document.createElement('div');
    div.className = 'col-md-2';
    div.appendChild(button);

    return { button, div };
}

function buildAccordionItem(attempt) {
    attempt.result === 'SUCCESS' ? hideButton("submitTask") : console.error('Test failed');
    const color = attempt.result === 'SUCCESS' ? "bg-success text-white" : "bg-danger text-white";
    const item = document.createElement('div');
    item.className = 'accordion-item';

    const header = document.createElement('h2');
    header.className = 'accordion-header';
    header.id = `heading_${attempt.id}`;

    const button = document.createElement('button');
    button.className = `accordion-button collapsed ${color}`;
    button.setAttribute('type', 'button');
    button.setAttribute('data-bs-toggle', 'collapse');
    button.setAttribute('data-bs-target', `#accordion_${attempt.id}`);
    button.setAttribute('aria-expanded', 'false');
    button.setAttribute('aria-controls', `accordion_${attempt.id}`);
    button.textContent = `Verification ${attempt.runNumber}`;
    header.appendChild(button);

    const collapse = document.createElement('div');
    collapse.id = `accordion_${attempt.id}`;
    collapse.className = 'accordion-collapse collapse';
    collapse.setAttribute('aria-labelledby', `heading_${attempt.id}`);
    collapse.setAttribute('data-bs-parent', '#accordionFlushExample');

    const body = document.createElement('div');
    body.className = 'accordion-body';
    body.innerHTML = `You can see details <a href="${attempt.detailsUrl}" target="_blank">here</a>`;

    if (attempt.testResultDto && attempt.testResultDto.length > 0) {
        attempt.testResultDto.forEach(testResult => {
            if (testResult && testResult.testCases && testResult.testCases.length > 0) {
                const nestedAccordion = document.createElement('div');
                nestedAccordion.className = 'accordion accordion-flush';
                nestedAccordion.id = `nestedAccordion_${attempt.id}`;

                testResult.testCases.forEach(testCase => {
                    const nestedItem = buildNestedAccordionItem(testCase, attempt.id);
                    nestedAccordion.appendChild(nestedItem);
                });

                body.appendChild(nestedAccordion);
            }
        })
    }

    collapse.appendChild(body);
    item.appendChild(header);
    item.appendChild(collapse);

    return item;
}

function buildNestedAccordionItem(testCase, parentAttemptId) {
    const color = testCase.failure ? "bg-danger text-white" : "bg-success text-white";
    const item = document.createElement('div');
    item.className = 'accordion-item';

    const header = document.createElement('h2');
    header.className = 'accordion-header';
    header.id = `case_heading_${parentAttemptId}_${testCase.id}`;

    const button = document.createElement('button');
    button.className = `accordion-button collapsed ${color}`;
    button.setAttribute('type', 'button');
    button.setAttribute('data-bs-toggle', 'collapse');
    button.setAttribute('data-bs-target', `#case_accordion_${parentAttemptId}_${testCase.id}`);
    button.setAttribute('aria-expanded', 'false');
    button.setAttribute('aria-controls', `case_accordion_${parentAttemptId}_${testCase.id}`);
    button.textContent = `Test Case: ${testCase.name}`;
    header.appendChild(button);

    const collapse = document.createElement('div');
    collapse.id = `case_accordion_${parentAttemptId}_${testCase.id}`;
    collapse.className = 'accordion-collapse collapse';
    collapse.setAttribute('aria-labelledby', `case_heading_${parentAttemptId}_${testCase.id}`);
    collapse.setAttribute('data-bs-parent', `#nestedAccordion_${parentAttemptId}`);

    const body = document.createElement('div');
    body.className = 'accordion-body';
    body.innerHTML = `
        <p>Classname: ${testCase.classname}</p>
        <p>Time: ${testCase.time}s</p>
    `;

    if (testCase.failure) {
        const failureDetails = document.createElement('div');
        failureDetails.innerHTML = `
            <p>Failure Message: ${testCase.failure.type}</p>
            <pre>${testCase.failure.details}</pre>
        `;
        body.appendChild(failureDetails);
    } else {
        body.innerHTML += `<p>Test passed successfully.</p>`;
    }

    collapse.appendChild(body);
    item.appendChild(header);
    item.appendChild(collapse);

    return item;
}

function addAccordionItem(attempt) {
    $("#accordionFlushExample").append(buildAccordionItem(attempt));
}

function addRepositoryCard(githubUrl, labId) {
    $("#repositoryCard").append(buildRepositoryCard(githubUrl));
    $(`#${START_TASK_BUTTON_ID}`).hide();

    const { button, div } = createSubmitTaskButton(labId);
    $("#submitTaskDiv").append(div);
    $(button).click(() => submitTask());
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $(`#${SUBMIT_TASK_BUTTON_ID}`).click(() => submitTask());
    $(`#${START_TASK_BUTTON_ID}`).click(() => startTask());
    $("#disconnect").click(() => disconnect());
});
