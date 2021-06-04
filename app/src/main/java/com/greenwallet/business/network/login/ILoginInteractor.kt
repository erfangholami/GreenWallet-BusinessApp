package com.greenwallet.business.network.login

import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.Subscriber

interface ILoginInteractor {

    fun login(email: String,
              password: String,
              listener: Subscriber<LoginResponse>
    ): Disposable?
}