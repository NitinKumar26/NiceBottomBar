package me.nitin.lib

import android.content.Context
import android.content.res.XmlResourceParser
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import org.xmlpull.v1.XmlPullParserException

class BottomBarParser(private val context: Context, res: Int) {

    private val parser: XmlResourceParser = context.resources.getXml(res)
    private val items: ArrayList<BottomBarItem> = arrayListOf()

    fun parse(): ArrayList<BottomBarItem> {
        try {
            var eventType: Int?
            do {
                eventType = parser.next()
                if (eventType == XmlResourceParser.START_TAG && "item" == parser.name) {
                    items.add(getTabConfig(parser))
                }
            } while (eventType != XmlResourceParser.END_DOCUMENT)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
            throw Exception()
        }
        return items
    }

    private fun getTabConfig(parser: XmlResourceParser): BottomBarItem {
        val attributeCount = parser.attributeCount
        var itemText: String? = null
        var itemDrawable: Drawable? = null
        for (i in 0 until attributeCount) {
            when (parser.getAttributeName(i)) {
                "title" -> itemText = getTitle(parser, i)
                "icon" -> itemDrawable = getIcon(parser, i)
            }
        }
        return BottomBarItem(itemText!!, itemDrawable!!)
    }

    private fun getIcon(parser: XmlResourceParser, i: Int): Drawable {
        return ContextCompat.getDrawable(context, parser.getAttributeResourceValue(i, 0))!!
    }

    private fun getTitle(parser: XmlResourceParser, i: Int): String {
        return context.getString(parser.getAttributeResourceValue(i, 0))
    }
}