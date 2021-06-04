package com.greenwallet.business.helper.network.login

import com.greenwallet.business.helper.network.Disposable
import com.greenwallet.business.helper.network.Subscriber

interface ILoginInteractor {

    fun login(
        email: String,
        password: String,
        listener: Subscriber<LoginResponse>
    ): Disposable?
}