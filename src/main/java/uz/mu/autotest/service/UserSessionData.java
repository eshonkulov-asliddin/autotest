package uz.mu.autotest.service;

import uz.mu.autotest.model.StudentTakenLab;

public record UserSessionData(String accessToken, StudentTakenLab studentTakenLab) {
}
