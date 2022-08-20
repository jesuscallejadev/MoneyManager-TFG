package com.calleja.jesus.moneymanager.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.calleja.jesus.moneymanager.R
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_licenses.*
import net.yslibrary.licenseadapter.Library
import net.yslibrary.licenseadapter.LicenseAdapter
import net.yslibrary.licenseadapter.Licenses
import java.util.ArrayList

class ShowLicensesActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_licenses)

            val licenses = ArrayList<Library>()


            // These libraries aren't hosted on GitHub (and you chose not to show the license text)
            licenses.add(
                Licenses.noContent(
                    "Android SDK", "Google Inc.",
                    "https://developer.android.com/sdk/terms.html"
                )
            )
            licenses.add(Licenses.noContent("Fabric", "Google Inc.", "https://fabric.io/terms"))

            licenses.add(Licenses.fromGitHubApacheV2("JakeWharton/ButterKnife"))
            licenses.add(Licenses.fromGitHubApacheV2("Hdodenhof/CircleImageView"))
            licenses.add(Licenses.fromGitHubApacheV2("Google/Dagger"))
            licenses.add(Licenses.fromGitHubApacheV2("Google/Gson"))
            licenses.add(Licenses.fromGitHubApacheV2("Immutables/Immutables"))
            licenses.add(Licenses.fromGitHubApacheV2("Square/LeakCanary"))
            licenses.add(Licenses.fromGitHubApacheV2("Square/OkHttp"))
            licenses.add(Licenses.fromGitHubApacheV2("Realm/Realm-Java"))
            licenses.add(Licenses.fromGitHubApacheV2("yqritc/RecyclerView-MultipleViewTypesAdapter"))
            licenses.add(Licenses.fromGitHubApacheV2("yshrsmz/LicenseAdapter"))
            licenses.add(Licenses.fromGitHubApacheV2("JakeWharton/ThreeTenABP"))
            licenses.add(Licenses.fromGitHubApacheV2("JakeWharton/Timber"))
            licenses.add(Licenses.fromGitHubApacheV2("GrandCentrix/Tray"))
            licenses.add(Licenses.fromGitHubApacheV2("Twitter/Twitter-Text"))
            licenses.add(Licenses.fromGitHubMIT("Jhy/Jsoup"))
            licenses.add(Licenses.fromGitHubBSD("Bumptech/Glide"))
            licenses.add(Licenses.fromGitHubBSD("Facebook/Stetho"))

            // This repository does not have license file
            licenses.add(Licenses.fromGitHub("GabrieleMariotti/ChangelogLib", Licenses.LICENSE_APACHE_V2))

            // These 2 licenses have a different branch name
            licenses.add(Licenses.fromGitHubApacheV2("ReactiveX/RxAndroid", "2.x/" + Licenses.FILE_AUTO))
            licenses.add(Licenses.fromGitHubApacheV2("ReactiveX/RxJava", "2.x/" + Licenses.FILE_AUTO))

            licenses.add(
                Licenses.noLink(
                    "Library without a link to the license, like Google Play Services",
                    "Author", "License name", "License content"
                )
            )

            linearLayoutManager = LinearLayoutManager(this)
            licenseListRecyclerView.layoutManager = linearLayoutManager
            licenseListRecyclerView.setHasFixedSize(true)
            licenseListRecyclerView.adapter = LicenseAdapter(licenses)

        }

}



