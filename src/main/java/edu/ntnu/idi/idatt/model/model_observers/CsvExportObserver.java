package edu.ntnu.idi.idatt.model.model_observers;

/**
 * <h1>CsvExportObserver</h1>
 *
 * Observer interface for classes that respond to CSV export events.
 * Implementations should define what happens when an export is requested.
 */
public interface CsvExportObserver {

  /**
   * <h2>onExportRequested</h2>
   *
   * Called when a CSV export operation is triggered by the subject.
   * Implementing classes should handle the file export logic here.
   */
  void onExportRequested();
}