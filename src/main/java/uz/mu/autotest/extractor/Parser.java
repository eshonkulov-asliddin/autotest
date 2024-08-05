package uz.mu.autotest.extractor;

import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.util.List;

public interface Parser<T> {
    List<T> parse(File file) throws JAXBException;
}
