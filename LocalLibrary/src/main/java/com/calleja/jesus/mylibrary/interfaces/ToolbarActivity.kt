package com.calleja.jesus.mylibrary.interfaces

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

open class ToolbarActivity : AppCompatActivity(), iToolbar{

    protected var toolbar: Toolbar? = null

    override fun toolbarToLoad(toolbar: Toolbar?) {
        this.toolbar = toolbar
        this.toolbar?.let { setSupportActionBar(this.toolbar) }
    }

    override fun enableHomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }
}