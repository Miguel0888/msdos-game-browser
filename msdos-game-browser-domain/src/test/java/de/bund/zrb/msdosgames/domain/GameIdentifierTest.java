package de.bund.zrb.msdosgames.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameIdentifierTest {

    @Test
    void rejectsBlankIdentifier() {
        assertThrows(IllegalArgumentException.class, new ExecutableBlock() {
            @Override
            public void execute() {
                GameIdentifier.of("   ");
            }
        });
    }

    @Test
    void trimsIdentifier() {
        assertEquals("doom", GameIdentifier.of(" doom ").getValue());
    }

    private interface ExecutableBlock extends org.junit.jupiter.api.function.Executable {
    }
}
