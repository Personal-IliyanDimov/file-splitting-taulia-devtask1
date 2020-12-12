package com.taulia.devtask1.io.reader.xml;

import com.taulia.devtask1.io.shared.xml.XmlNodeNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class BasicXmlReader {

    private final File inputFile;

    public void process(Set<String> supportedElements, Consumer<Map<String, String>> recordConsumer) throws Exception {
        // security configuration can be configured also if required
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);

        final XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(inputFile));

        try {
            Map<String,String> map = null;

            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    final StartElement startElement = nextEvent.asStartElement();
                    final String localPart = startElement.getName().getLocalPart();

                    if (XmlNodeNames.ELEMENT_ROW.equals(localPart)) {
                        map = new HashMap<>();
                    }
                    else {

                        if (supportedElements.contains(localPart)) {
                            nextEvent = reader.nextEvent();
                            if (nextEvent instanceof Characters) {
                                map.put(localPart, readCharacterData((Characters) nextEvent));
                            }
                        }
                    }
                }
                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals(XmlNodeNames.ELEMENT_ROW)) {
                        recordConsumer.accept(map);
                    }
                }
            }
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private String readCharacterData(Characters characterEvent) {
        return characterEvent.asCharacters().getData();
    }
}
