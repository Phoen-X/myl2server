package com.vvygulyarniy.l2.gameserver.world.config.npc;

import com.vvygulyarniy.l2.domain.geo.Position;
import lombok.Getter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Phoen-X on 12.03.2017.
 */
public class XmlNpcSpawnInfoParser {

    private final Path sourceFile;
    @Getter
    private final List<Npc> parsedData;

    public XmlNpcSpawnInfoParser(Path sourceFile) throws JDOMException, IOException {
        List<Npc> npcList = new ArrayList<>(100);
        this.sourceFile = sourceFile;
        Document document = new SAXBuilder().build(Files.newInputStream(sourceFile));
        XPathExpression<Element> expression = XPathFactory.instance().compile("//npc_info_list/npc", Filters.element());
        for (Element npcItem : expression.evaluate(document)) {
            int id = npcItem.getAttribute("id").getIntValue();
            String name = npcItem.getAttributeValue("name");
            int level = npcItem.getAttribute("level").getIntValue();
            int respawnTime = npcItem.getAttribute("respawnTime").getIntValue();
            Element spawnPositionItem = npcItem.getChild("spawn_position");
            if (spawnPositionItem == null) {
                throw new IllegalArgumentException("Element " + npcItem + " does not have spawn_position child");
            }

            Position spawnPosition = new Position(spawnPositionItem.getAttribute("x").getIntValue(),
                                                  spawnPositionItem.getAttribute("y").getIntValue(),
                                                  spawnPositionItem.getAttribute("z").getIntValue(),
                                                  spawnPositionItem.getAttribute("heading").getIntValue());


            npcList.add(new Npc(id, name, level, spawnPosition, Duration.ofSeconds(respawnTime)));
        }
        this.parsedData = npcList;
    }

    private static class NpcInfoList {
        @Getter
        private List<Npc> npc;
    }
}
