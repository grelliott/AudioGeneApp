package ca.grantelliott.audiogeneapp.ui.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ca.grantelliott.audiogeneapp.R
import kotlin.math.PI

class GaugeComponent : View {
    private var _value: Float = 0f
    private var _minValue: Float = 0f
    private var _maxValue: Float = 1.0f
    private var _warnValue: Float = 0f
    private var _criticalValue: Float = 0f
    private var _thickness: Float = 0f
    private var _goodColor: Int = Color.GREEN
    private var _warnColor: Int = Color.rgb(255, 191, 0)  // Amber
    private var _criticalColor: Int = Color.RED

    private var _oval: RectF = RectF()
    private var _startAngle: Float = 0f
    private var _sweepAngle: Float = 0f
    private var _color: Int = _goodColor
    private val _path: Path = Path()
    private var _paint: Paint = Paint()

    var value: Float
        get() = _value
        set(value) {
            _value = value
            invalidatePaintAndMeasurements()
        }
    private var minValue: Float
        get() = _minValue
        set(minValue) {
            _minValue = minValue
            invalidatePaintAndMeasurements()
        }
    private var maxValue: Float
        get() = _maxValue
        set(maxValue) {
            _maxValue = maxValue
            invalidatePaintAndMeasurements()
        }
    private var warnValue: Float
        get() = _warnValue
        set(warnValue) {
            _warnValue = warnValue
            invalidatePaintAndMeasurements()
        }
    private var criticalValue: Float
        get() = _criticalValue
        set(criticalValue) {
            _criticalValue = criticalValue
            invalidatePaintAndMeasurements()
        }
    private var goodColor: Int
        get() = _goodColor
        set(goodColor) {
            _goodColor = goodColor
            invalidatePaintAndMeasurements()
        }
    private var warnColor: Int
        get() = _warnColor
        set(warnColor) {
            _warnColor = warnColor
            invalidatePaintAndMeasurements()
        }
    private var criticalColor: Int
        get() = _criticalColor
        set(criticalColor) {
            _criticalColor = criticalColor
            invalidatePaintAndMeasurements()
        }
    private var thickness: Float
        get() = _thickness
        set(thickness) {
            _thickness = thickness
            invalidatePaintAndMeasurements()
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.GaugeComponent, defStyle, 0
        )

        _value = a.getFloat(
            R.styleable.GaugeComponent_value,
            value
        )
        _minValue = a.getFloat(
            R.styleable.GaugeComponent_minValue,
            minValue
        )
        _maxValue = a.getFloat(
            R.styleable.GaugeComponent_maxValue,
            maxValue
        )
        _warnValue = a.getFloat(
            R.styleable.GaugeComponent_warnValue,
            warnValue
        )
        _criticalValue = a.getFloat(
            R.styleable.GaugeComponent_criticalValue,
            criticalValue
        )
        _goodColor = a.getColor(
            R.styleable.GaugeComponent_goodColor,
            goodColor
        )
        _warnColor = a.getColor(
            R.styleable.GaugeComponent_warnColor,
            warnColor
        )
        _criticalColor = a.getColor(
            R.styleable.GaugeComponent_criticalColor,
            criticalColor
        )
        _thickness = a.getDimension(
            R.styleable.GaugeComponent_thickness,
            thickness
        )

        a.recycle()

        // Set up drawing objects
        _paint = Paint().apply {
            strokeWidth = thickness
            color = goodColor
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }

        // Calculate dimensions and colours needed to draw
        invalidatePaintAndMeasurements()
    }

    private fun invalidatePaintAndMeasurements() {
        calculateStartAngle()
        calculateSweepAngle()
        determineStrokeColor()

        _path.reset()
        _paint?.let {
            it.color = _color
        }
        invalidate()
    }

    private fun determineStrokeColor() {
        _color = when {
            (criticalValue > minValue && value > criticalValue) -> criticalColor
            (warnValue > minValue && value > warnValue) -> warnColor
            else -> goodColor
        }
    }

    private fun calculateSweepAngle() {
        _sweepAngle = 180 * (value / (maxValue - minValue))
    }

    private fun convertRadsToDegrees(rad: Float): Float {
        return rad * 180 / PI.toFloat()
    }

    private fun calculateStartAngle() {
        _startAngle = 180f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val padding = _paint.strokeWidth
        val size = (if (width < height) width else height) - (2*padding)
        val radius = size / 2

        val rectLeft = (width - (2 * padding)) / 2 - radius + padding
        val rectTop = (height - (2 * padding)) / 2 - radius + padding
        val rectRight = rectLeft + size
        val rectBottom = rectTop + size
        _oval.set(rectLeft, rectTop, rectRight, rectBottom)
        _path.addArc(_oval, _startAngle, _sweepAngle)
        canvas?.drawPath(_path, _paint)
    }
}