# Java Graphics Editor

A fully interactive pixel-based graphics editor built in Java Swing, featuring a **multi-layer architecture**, **seven drawing tools**, **pixel-level image filters**, **layer compositing**, and **animation playback**. Built from scratch with custom pixel manipulation, Bresenham's line algorithm, and a timer-driven animation engine.

---

## Overview

This project implements a desktop graphics application comparable in structure to tools like MS Paint — but with multi-layer support, geometric drawing tools, and real-time animation. All pixel operations are handled manually via RGBA arrays rather than relying on high-level graphics abstractions.

---

## Architecture

```
Driver
  └── Menu (canvas size input)
        └── GUI (main application window)
              ├── LayerManager      (state — layers, colours, compositing)
              │     └── Layer[]     (RGBA pixel arrays per layer)
              ├── ToolManager       (mouse input → drawing operations)
              ├── LayerFilterer     (image filters on active layer)
              ├── MyCanvas          (Swing component — render + animation)
              └── MenuBar           (file I/O — save PNG, open image)
```

---

## Components

### `LayerManager.java`
The core state manager. Maintains a `LinkedList<Layer>` with per-layer visibility toggling. Key operations:
- `addLayer()` / `deleteLayer()` / `moveLayerUp()` / `moveLayerDown()`
- `combineLayers()` — composites all visible layers into a single RGBA frame for rendering
- `setPixelArray()` — writes a list of coordinates to the active layer with the current colour and brush size
- `eyedrop()` — samples the active layer colour at a given pixel
- `getImage()` / `getThumbnail()` — produces `BufferedImage` outputs for rendering and animation

### `Layer.java`
A single drawing layer backed by a 3D `int[height][width][4]` RGBA array. Supports per-pixel read/write and full layer clear.

### `ToolManager.java`
Handles all mouse input and maps it to drawing operations via an enum-based tool switcher:

| Tool | Behaviour |
|---|---|
| `FREEHAND` | Continuous stroke via Bresenham's line between drag points |
| `ERASE` | Same as freehand with alpha=0 |
| `LINE` | Straight line from press to release |
| `RECT` | Hollow rectangle via four edge lines |
| `CIRCLE` | Ellipse using parametric point-in-ellipse test |
| `TRIANGLE` | Filled triangle via 3-click point selection and area-equality test |
| `EYEDROP` | Samples pixel colour into active colour |

**Line algorithm** — all stroke-based tools use a custom implementation of Bresenham's line algorithm:
```java
while (true) {
    coordinates.add(new int[] {x0, y0});
    if (x0 == x1 && y0 == y1) break;
    e2 = 2 * err;
    if (e2 > -dy) { err -= dy; x0 += sx; }
    if (e2 < dx)  { err += dx; y0 += sy; }
}
```

### `LayerFilterer.java`
Applies non-destructive image filters to the active layer:
- **Greyscale** — luminosity-weighted conversion: `0.3R + 0.59G + 0.11B`
- **Mirror Horizontal** — pixel array reflected across vertical axis
- **Mirror Vertical** — pixel array reflected across horizontal axis
- **Fill** — channel summation fill effect

### `MyCanvas.java`
Custom `JComponent` that renders the composited layer image. Contains a `Timer`-driven animation engine that cycles through layers as frames at 500ms intervals — turning the layer stack into a flipbook-style animation.

### `MenuBar.java`
File I/O — saves the current canvas as a PNG using `ImageIO.write()` with a `JFileChooser` dialog.

---

## Features

- Multi-layer editing with visibility toggling and layer reordering
- Seven drawing tools with adjustable brush size
- Image filters: greyscale, horizontal/vertical mirror
- Layer compositing with transparency support
- Animation playback — layers cycle as frames at 2fps
- PNG export

---

## Tech Stack

| Technology | Role |
|---|---|
| Java Swing | GUI framework |
| `java.awt.image.BufferedImage` | Canvas rendering |
| `javax.imageio.ImageIO` | PNG file I/O |
| `java.util.LinkedList` | Layer stack management |
| `java.util.Timer` | Animation frame scheduling |
| Bresenham's Algorithm | Pixel-accurate line/stroke drawing |

---

## Running the Project

```bash
javac *.java
java Driver
```

Enter canvas width and height in the launch menu, then start drawing.

---

## Skills Demonstrated

- Custom pixel manipulation and RGBA array management
- Bresenham's line algorithm implementation
- Multi-layer compositing with transparency
- Geometric algorithms (ellipse, triangle area test)
- Java Swing GUI and event-driven programming
- Timer-based animation engine

---

## Author

**Jayson** — [github.com/jayson147](https://github.com/jayson147)
