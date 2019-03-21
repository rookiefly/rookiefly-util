package com.rookiefly.commons.imagetool;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.plaf.metal.MetalLookAndFeel;

@SuppressWarnings("restriction")
public abstract interface LookAndFeel {
    public static final String WINDOWS_LOOKANDFEEL = WindowsLookAndFeel.class.getName();
    public static final String METAL_LOOKANDEEL = MetalLookAndFeel.class.getName();
}
