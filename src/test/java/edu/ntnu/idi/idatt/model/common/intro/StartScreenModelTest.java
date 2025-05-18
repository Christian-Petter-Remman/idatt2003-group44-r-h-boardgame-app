package edu.ntnu.idi.idatt.model.common.intro;

import edu.ntnu.idi.idatt.view.common.intro.dialogs.DialogConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class StartScreenModelTest {

  private StartScreenModel model;

  @BeforeEach
  void setUp() {
    model = new StartScreenModel();
  }

  @Test
  void testDialogsAreLoadedOnInit() {
    Map<String, DialogConfig> dialogs = model.getDialogs();
    assertNotNull(dialogs, "Dialogs should be loaded on construction");
    assertFalse(dialogs.isEmpty(), "Dialogs should not be empty if JSON file is valid");
  }

  @Test
  void testPropertyChangeListenerIsFired() {
    AtomicReference<PropertyChangeEvent> receivedEvent = new AtomicReference<>();

    class TestableStartScreenModel extends StartScreenModel {
      public void reloadDialogs() {
        super.loadDialogs();
      }
    }

    TestableStartScreenModel model = new TestableStartScreenModel();
    model.addListener(receivedEvent::set);

    model.reloadDialogs();

    assertNotNull(receivedEvent.get(), "PropertyChangeEvent should have been fired");
    assertEquals("dialogs", receivedEvent.get().getPropertyName());
    assertNull(receivedEvent.get().getOldValue());
    assertNotNull(receivedEvent.get().getNewValue());
  }
}
