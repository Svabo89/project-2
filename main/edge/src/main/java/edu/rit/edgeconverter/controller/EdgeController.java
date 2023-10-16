package edu.rit.edgeconverter.controller;

import edu.rit.edgeconverter.view.ConverterGUI;

/**
 * Hey there! 👋 This is our nifty EdgeController.
 * It's like the middleman between our cool ConverterGUI and, well, everything
 * else!
 */
public class EdgeController {

  // Here's where we keep a reference to our GUI. It's like its cozy little home.
  private ConverterGUI view;

  /**
   * When a new EdgeController is born, it needs a ConverterGUI to talk to.
   * It's like its buddy that it hangs out with all the time.
   *
   * @param view The ConverterGUI that this controller will be chit-chatting with.
   */
  public EdgeController(ConverterGUI view) {
    this.view = view;

    // Let's get this party started! 🎉 We tell the view to start doing its thing.
    this.view.start();
  }

  /**
   * Alright, so this method is a bit lonely right now, but don't worry!
   * This is where we'll add all those fancy listeners to catch user actions.
   * It's like setting up a party where we're waiting for guests to arrive. 🥳
   */
  public void addListeners() {
    // TODO: Make this method less lonely. Add some listeners, perhaps?
  }
}
