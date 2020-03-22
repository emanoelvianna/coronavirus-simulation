package com.coronavirus.ui;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class LegendGUI extends Canvas {

  private static final long serialVersionUID = 1L;

  public void paint(Graphics legend) {

    Graphics2D leg = (Graphics2D) legend;
    leg.scale(0.7, 0.7);
    Line2D line = new Line2D.Double(20, 60, 70, 70);
    leg.setColor(Color.lightGray);
    leg.setStroke(new BasicStroke(3));
    leg.draw(line);

    /* agent */
    leg.setColor(Color.BLACK);
    leg.fillOval(20, 150, 20, 20);

    leg.setColor(Color.YELLOW);
    leg.fillOval(20, 180, 20, 20);

    leg.setColor(Color.PINK);
    leg.fillOval(20, 210, 20, 20);

    leg.setColor(Color.ORANGE);
    leg.fillOval(20, 240, 20, 20);

    leg.setColor(Color.RED);
    leg.fillOval(20, 270, 20, 20);

    leg.setColor(Color.BLUE);
    leg.fillOval(20, 300, 20, 20);

    /* camps */
    leg.setColor(new Color(224, 255, 224));
    leg.fillRect(20, 370, 30, 30);

    leg.setColor(new Color(255, 180, 210));
    leg.fillRect(20, 405, 30, 30);

    leg.setColor(new Color(204, 204, 153));
    leg.fillRect(20, 440, 30, 30);

    /* facilities */
    leg.setColor(new Color(0, 255, 0));
    leg.drawRect(20, 520, 30, 30);

    leg.setColor(new Color(0, 0, 102));
    leg.drawRect(20, 560, 30, 30);

    leg.setColor(new Color(0, 102, 102));
    leg.drawRect(20, 600, 30, 30);

    leg.setColor(new Color(255, 0, 0));
    leg.drawRect(20, 640, 30, 30);

    Font f = new Font("Serif", Font.BOLD, 24);
    leg.setFont(f);

    leg.setColor(Color.black);

    leg.drawString("LEGEND", 60, 40);

    Font f2 = new Font("Serif", Font.BOLD, 18);
    leg.setFont(f2);

    leg.setColor(Color.black);

    leg.drawString("Agent's Status", 20, 130);
    leg.drawString("Refugee Camps", 20, 350);
    leg.drawString("Facility", 20, 500);

    Font f3 = new Font("Serif", Font.PLAIN, 20);
    leg.setFont(f3);
    leg.setColor(Color.black);

    leg.drawString("Road", 90, 80);

    leg.drawString("Susceptible", 70, 165);
    leg.drawString("Exposed", 70, 195);
    leg.drawString("Mild Infection", 70, 225);
    leg.drawString("Severe Infection", 70, 255);
    leg.drawString("Toxic Infection", 70, 285);
    leg.drawString("Recovered", 70, 315);

    leg.drawString("Dagahaley", 70, 395);
    leg.drawString("Ifo", 70, 430);
    leg.drawString("Hagadera", 70, 465);

    leg.drawString("School", 70, 545);
    leg.drawString("Mosque", 70, 585);
    leg.drawString("Market", 70, 625);
    leg.drawString("Health Center", 70, 665);

  }
}
