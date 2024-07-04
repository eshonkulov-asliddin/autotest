const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:9091/mu-autotest'
});


function enableLoading(){
    const submitButton = document.getElementById('submitTask');
    submitButton.disabled = true;
    const spinner = document.getElementById("submitTaskSpinner");
    spinner.style.display = "inline-block";
}

function disableLoading(){
    const submitButton = document.getElementById('submitTask');
    submitButton.disabled = false;
    const spinner = document.getElementById("submitTaskSpinner");
    spinner.style.display = "none";
    sessionStorage.removeItem('taskInProgress');
    sessionStorage.removeItem('labId');
    sessionStorage.setItem('callBuildAndTestRepository', 'false');
}

function reconnectIfNecessary() {
    if (sessionStorage.getItem('taskInProgress') === 'true') {
        enableLoading();
        stompClient.activate();
        // submit task websocket connection
        stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/user/queue/attempts', (response) => {
                console.log(JSON.parse(response.body));
                addAccordionItem(JSON.parse(response.body))
                disconnect();
                disableLoading();
            });
        };
    }
}

window.addEventListener('load', reconnectIfNecessary);
function submitTask(callback) {
    const labId = parseInt($("#submitTask").data("lab-id"));
    console.log("lab id", labId)
    enableLoading();
    sessionStorage.setItem('taskInProgress', 'true');
    sessionStorage.setItem('labId', labId);

    stompClient.activate();
    // submit task websocket connection
    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/attempts', (response) => {
            console.log(JSON.parse(response.body));
            addAccordionItem(JSON.parse(response.body))
            disconnect();
            disableLoading();
        });
        buildAndTestRepository();
    };

    stompClient.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
        disableLoading();

    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
        disableLoading();
    };
}

function startTask(callback) {
    stompClient.activate();
    // generate repository websocket connection
    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/repositories', (response) => {
            var userTakenLab = JSON.parse(response.body);
            console.log(userTakenLab);
            const id = userTakenLab.id;
            const githubUrl = userTakenLab.githubUrl;
            const status = userTakenLab.status;
            const courseId = userTakenLab.courseId;
            const labId = userTakenLab.labId;
            console.log("Started Lab: ", userTakenLab);
            addRepositoryCard(githubUrl, labId);
            disconnect();
        });
        generateRepository();
    };

    stompClient.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    };

}

function disconnect() {
    stompClient.deactivate();
    console.log("Disconnected");
}

function buildAndTestRepository() {
    if (stompClient.connected) {
        let labId = parseInt($("#submitTask").data("lab-id"));
        console.log(labId);
        stompClient.publish({
            destination: "/app/submitTask",
            body: JSON.stringify({'labId': labId})
        });
    } else {
        // Handle case where WebSocket connection is not yet established
        console.error('WebSocket connection not established');
    }
}

function generateRepository() {
    if (stompClient.connected) {
        let labId = parseInt($("#startTask").data("not-started-lab-id"));
        console.log(labId);
        stompClient.publish({
            destination: "/app/generateRepository",
            body: JSON.stringify({'labId': labId})
        });
    } else {
        // Handle case where WebSocket connection is not yet established
        console.error('WebSocket connection not established');
    }
}

const buildRepositoryCard = function(githubUrl) {
    console.log("Constructing repository card ...");
    let cardDiv = document.createElement("div");
    cardDiv.classList.add("card");

    let cardBodyDiv = document.createElement("div");
    cardBodyDiv.classList.add("card-body");

    let cardTitle = document.createElement("h5");
    cardTitle.classList.add("card-title");
    cardTitle.textContent = "GitHub Repository";

    let cardText = document.createElement("p");
    cardText.classList.add("card-text");
    cardText.textContent = "Check out task on GitHub:";

    let cardLink = document.createElement("a");
    cardLink.setAttribute("href", githubUrl);
    cardLink.setAttribute("target", "_blank");
    cardLink.classList.add("btn", "btn-primary");
    cardLink.textContent = "GitHub Repository";

    // Appending elements
    cardBodyDiv.appendChild(cardTitle);
    cardBodyDiv.appendChild(cardText);
    cardBodyDiv.appendChild(cardLink);

    cardDiv.appendChild(cardBodyDiv);

    return cardDiv;

}

function createSubmitTaskButton(labId) {
    let button = document.createElement('button');
    button.id = 'submitTask';
    button.className = 'btn btn-dark btn-block';
    button.dataset.labId = labId;

    // Create the icon
    let icon = document.createElement('i');
    icon.className = 'fab fa-github mr-1';

    // Append the icon and text to the button
    button.appendChild(icon);
    button.appendChild(document.createTextNode(' Submit'));

    // Create the div
    let div = document.createElement('div');
    div.className = 'col-md-2';

    // Append the button to the div
    div.appendChild(button);

    return div;
}

const buildAccordionItem = function (id, result, title, descriptionUrl) {
    let color;
    if (result === 'SUCCESS') {
        color = "bg-success text-white";
    } else {
        color = "bg-danger text-white";
    }
    let item = document.createElement('div');
    item.className = 'accordion-item';

    let header = document.createElement('h2');
    header.className = 'accordion-header';
    header.id = 'heading_' + id;

    let button = document.createElement('button');
    button.className = `accordion-button collapsed ${color}`;
    button.setAttribute('type', 'button');
    button.setAttribute('data-bs-toggle', 'collapse');
    button.setAttribute('data-bs-target', '#accordion_' + `${id}`);
    button.setAttribute('aria-expanded', 'false');
    button.setAttribute('aria-controls', 'flush-collapseOne_' + id);
    button.textContent = title;

    header.appendChild(button);

    let collapse = document.createElement('div');
    collapse.id = "accordion_" + id;
    collapse.className = 'accordion-collapse collapse';

    let body = document.createElement('div');
    body.className = 'accordion-body ' + color;
    body.textContent = "You can see details "
    body.style.whiteSpace = 'normal';

    // Append the content generated by generateInnerAccordion
    const a = document.createElement("a")
    a.href = descriptionUrl;
    a.textContent = "here";
    a.setAttribute("target", "_blank");
    body.appendChild(a);

    collapse.appendChild(body);

    item.appendChild(header);
    item.appendChild(collapse);

    return item;
}

function addAccordionItem(attempt) {
    $("#accordionFlushExample").append(buildAccordionItem(attempt.id, attempt.result, "Verification " + attempt.runNumber, attempt.detailsUrl));
}

function addRepositoryCard(githubUrl, labId) {
    $("#repositoryCard").append(buildRepositoryCard(githubUrl));
    $("#startTask").hide();
    $("#submitTaskDiv").append(createSubmitTaskButton(labId));
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#submitTask" ).click(() => submitTask());
    $( "#startTask" ).click(() => startTask());
    $( "#disconnect" ).click(() => disconnect());
});
$(document).on('click', '#submitTask', function() {
    submitTask();
});