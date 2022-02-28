package com.example.offiqlresturantapp.use_case

import com.example.offiqlresturantapp.data.login.model.api.ApKLoginPost
import com.example.offiqlresturantapp.data.login.model.api.ApkBody
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.checkFieldValue
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor() {

    fun getLoginResponse(userID: String, password: String, storeNo: String) = flow {
        val data =
            if (checkFieldValue(userID) || checkFieldValue(password) || checkFieldValue(storeNo)) {
                ApisResponse.Loading("Please Enter the Correct Info")
            } else {
                val data = ApKLoginPost(
                    apK = ApkBody(
                        storeNo = storeNo,
                        userID = userID,
                        password = password
                    )
                )
                ApisResponse.Success(data)
            }
        emit(data)
    }


}