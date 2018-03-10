package com.example.artem.speakup.Authentication

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by Artem on 10.03.2018.
 */
@IgnoreExtraProperties
public class Session (var listOfParts :List<Part>) {
    class Part(var name: String)
}