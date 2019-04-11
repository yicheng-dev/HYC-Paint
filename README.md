# HYC-Paint

A painting tool developed with Java and it can draw lines, polygons, ellipses, curves and do some geometric transformation to them. It is Broccoli's course assignment for Computer Graphics in Nanjing University. It will be developing until late June. Code-level plagiarism is not allowed while ideas' borrowing is allowed.

## Environment Configuration

`JDK8/Java8` and newer versions are enough, since `JavaFX` has been included in them.

## How to compile and run?

- You can open the project in `Intellij IDEA` and run `Main`.
- A shell bash `run.sh` is provided so you can type `$ bash run.sh` to compile and run it. At current stage, two CLI parameters are needed, representing the path of input file and the path of image output directory. For example, 

    ```
    $ bash run.sh input.txt ./ -f
    ```
- You can also launch a python-like CLI by

	```
	$ bash run.sh input.txt ./ -c
	```

## Front End

At current stage, the GUI of HYC-Paint has not been developed so only CLI is supported. You can input instructions into your input file (designated by you). The following instructions are a good example. The annotations beginning with `%` should not appear in real files.

```
resetCanvas 400 300     % Clear and reset the canvas to 400 * 300
setColor 255 0 255      % Set the color of brush to Magenta with RGB(255, 0, 255)
drawLine 1 50 50 100 100 DDA    % Use the DDA Algorithm to draw a line with begin point (50, 50) and end point (100, 100). Its id is 1.
setColor 255 255 0
drawLine 2 100 50 50 100 DDA
setColor 120 120 1
drawPolygon 3 4 DDA     % Use the DDA Algorithm to draw a polygon with four vertices: (150, 150), (150, 200), (200, 200) and (200, 150)
150 150 150 200 200 200 200 150
drawEllipse 4 200 150 50 40     % Draw an ellipse with center point (200, 150) and 50 long semi-major axis, 40 long semi-minor axis
setColor 0 230 111
drawCurve 5 3 Bezier    % Draw a Bezier with 3 control points
10 10 200 70 98 122
translate 1 110 110     % Translate a graph unit with id=1 by 110 in x-axis and 110 in y-axis
translate 1 -110 -110
translate 3 -80 -80
translate 3 150 150
translate 5 180 100
rotate 1 75 75 60       % Rotate a graph unit with id=1 by 60 degrees around (75, 75)
rotate 3 245 245 -435
rotate 5 0 0 -5
scale 1 0 0 1.5         % Scale a graph unit with id=1 by 1.5 times around (0, 0)
scale 3 200 200 0.5
drawCurve 10 4 Bezier
28 34 9 86 61 4 129 42
drawCurve 11 4 Bezier
114 88 60 92 99 3 42 24
scale 11 0 0 2
translate
1 280 0
drawPolygon 20 4 DDA 70 60 70 80 90 80 90 60
clip 2 70 60 90 80 Liang-Barsky     % Use The Liang-Barsky Algorithm to clip line (id=2) according to a rect with bottom-left vertice (70, 60) and up-right vertice (90, 80)
drawLine 9 90 40 40 100 DDA
clip 9 70 60 90 80 Liang-Barsky
drawLine 40 80 10 80 200 DDA
clip 40 70 60 90 80 Cohen-Sutherland
drawLine 88 10 70 200 70 DDA
clip 88 70 60 90 80 Cohen-Sutherland
saveCanvas output.bmp       % Save the canvas to output file [Ouput_dir + output.bmp]
exit       % Exit

```

- `JavaFX` is being used to optimize the front-end.

## Back End

In addition to the classes used for the project architecture, `CGAlgorithm.java` Encapsulates algorithms for generating various graphic entities.

## Output Example

![](image/output.bmp)

## License

This software is licensed under the MIT license. © NJUBroccoli