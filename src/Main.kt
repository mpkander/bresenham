import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.abs
import java.awt.Color

const val width = 260
const val height = 260
const val mainRgb = -1

fun main(args: Array<String>) {
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    val rgb = Color(65, 65, 65).rgb

    drawLine(width / 2, height - height, width / 2, height, image, rgb)
    drawLine(width - width, height / 2, width, height / 2, image, rgb)
    drawLine(width - width, height - height, width, height, image, rgb)
    drawLine(width - width, height, width, height - height, image, rgb)

    drawCircle(38, 92, 38, image)
    drawLine(width / 2, height / 2, height - height, 65, image)

    try {
        val file = File("image.png")
        ImageIO.write(image, "png", file)
    } catch (e: IOException) {
        println(e)
    }
}

fun drawLine(_x0: Int, _y0: Int, _x1: Int, _y1: Int, image: BufferedImage, color: Int = mainRgb) {
    var x0 = _x0
    var y0 = _y0
    var x1 = _x1
    var y1 = _y1
    var steep = false

    if (abs(x0-x1) < abs(y0-y1)) {
        x0 = y0.also { y0 = x0 }
        x1 = y1.also { y1 = x1 }
        steep = true
    }

    if (x0 > x1) {
        x0 = x1.also { x1 = x0 }
        y0 = y1.also { y1 = y0 }
    }

    val dx = x1 - x0
    val dy = y1 - y0
    val deltaError = abs(dy) * 2
    var error = 0
    var y = y0
    for (x in x0..x1) {
        if (steep) {
            drawPixel(y, x, image, color)
        } else {
            drawPixel(x, y, image, color)
        }
        error += deltaError

        if (error > dx) {
            y += if (y1 > y0) 1 else -1
            error -= dx*2
        }
    }
}

fun drawCircle(xCenter: Int, yCenter: Int, radius: Int, image: BufferedImage) {
    var x = 0
    var y = radius
    var delta = 3 - 2 * radius

    while (x < y) {
        drawCirclePixels(x, y, xCenter, yCenter, image)
        drawCirclePixels(y, x, xCenter, yCenter, image)
        if (delta < 0) {
            delta += 4 * x + 6
        } else {
            delta += 4 * (x-y) + 10
            y--
        }
        x++
    }
    if (x == y) drawCirclePixels(x, y, xCenter, yCenter, image)
}

fun drawCirclePixels(x: Int, y: Int, xCenter: Int, yCenter: Int, image: BufferedImage) {
    drawPixel(xCenter + x, yCenter + y, image)
    drawPixel(xCenter + x, yCenter - y, image)
    drawPixel(xCenter - x, yCenter + y, image)
    drawPixel(xCenter - x, yCenter - y, image)
}

fun drawPixel(x: Int, y: Int, image: BufferedImage, rgb: Int = mainRgb) {
    if (x < 0 || y < 0 || x >= image.width || y >= image.height)
        return println("Пиксель в координате ($x, $y) не нарисован: выход за границу")

    image.setRGB(x, y, rgb)
}

