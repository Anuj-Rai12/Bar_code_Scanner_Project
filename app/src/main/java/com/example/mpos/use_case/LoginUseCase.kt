package com.example.mpos.use_case


import com.example.mpos.data.login.model.api.ApKLoginPost
import com.example.mpos.data.login.model.api.ApkBody
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.checkFieldValue
import kotlinx.coroutines.flow.flow

class LoginUseCase {

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