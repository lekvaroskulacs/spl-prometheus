/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package View;


import java.awt.*;

import javax.swing.*;

/* CharacterCreation.java requires no other files. */

/**
 * The class was implemented from:
 * <a href="https://docs.oracle.com/javase/tutorial/uiswing/components/colorchooser.html">Java Color Picker</a>
 */
public class ColorPicker extends JPanel {
    private JColorChooser tcc;
    private  JTextField userName;

    /**
     * constructor - creates a text field for the username, a color picker area
     */
    public ColorPicker() {
        super(new BorderLayout());

        // creating a text field for the player's name
        userName = new JTextField("User");
        userName.setBounds(new Rectangle(300, 100));
        JPanel userNamePanel = new JPanel(new BorderLayout());
        userNamePanel.setBorder(BorderFactory.createTitledBorder("Username:"));
        userNamePanel.add(userName, BorderLayout.CENTER);

        // creating a panel for choosing color
        tcc = new JColorChooser();
        tcc.setBorder(BorderFactory.createTitledBorder("Choose Ship Color"));

        // embedding the elements
        add(userNamePanel, BorderLayout.NORTH);
        add(tcc, BorderLayout.CENTER);

    }

    /**
     * getUserColor - for saving the color of the ship
     *
     * @return the color of the ship
     */
    public Color getUserColor() {
        return tcc.getColor();
    }
}