package uz.mu.autotest.extractor;

import jakarta.xml.bind.JAXBException;

import java.io.File;

public interface Parser<T> {
    T parse(File file) throws JAXBException;
}
