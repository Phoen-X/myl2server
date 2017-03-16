package com.vvygulyarniy.l2.gameserver.world.config.npc;

import com.vvygulyarniy.l2.domain.geo.Position;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Phoen-X on 12.03.2017.
 */
public class JsonNpcSpawnInfoParserTest {

    @Test
    public void shouldParseExistingFile() throws Exception {
        XmlNpcSpawnInfoParser parser = new XmlNpcSpawnInfoParser(Paths.get(ClassLoader.getSystemResource(
                "npc_info.xml")
                                                                                      .toURI()));
        assertThat(parser.getParsedData()).containsOnly(new Npc(1,
                                                                "Some Mob",
                                                                13,
                                                                new Position(1, 1, 1, 13),
                                                                Duration.ofSeconds(30)),
                                                        new Npc(2,
                                                                "Some Mob 2",
                                                                14,
                                                                new Position(2, 2, 2, 14),
                                                                Duration.ofSeconds(60)));
    }
}