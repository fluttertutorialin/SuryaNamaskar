package com.jdkgroup.suryanamaskar.activity

import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import com.jdkgroup.baseclass.SimpleMVPActivity
import com.jdkgroup.constant.RestConstant
import com.jdkgroup.model.api.countrylist.CountryResponse
import com.jdkgroup.model.api.countrylist.ModelCountry
import com.jdkgroup.model.api.signup.SignUpResponse
import com.jdkgroup.model.request.SignUpRequest
import com.jdkgroup.presenter.SignUpPresenter
import com.jdkgroup.suryanamaskar.DrawerActivity
import com.jdkgroup.suryanamaskar.R
import com.jdkgroup.suryanamaskar.dialog.SpDialogCountry
import com.jdkgroup.utils.AppUtils
import com.jdkgroup.utils.Logging
import com.jdkgroup.utils.Preference
import com.jdkgroup.utils.Validator
import com.jdkgroup.view.SignUpView

class SignUpActivity : SimpleMVPActivity<SignUpPresenter, SignUpView>(), SignUpView {
    private var listCountry: List<ModelCountry>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        hideSoftKeyboard()

        var appBtnSignUp = findViewById<AppCompatButton>(R.id.appBtnSignUp)

        //TODO SIGN UP
        appBtnSignUp.setOnClickListener({
            var userName = appEdiTextGetString(R.id.appEdtUserName)
            val email =  appEdiTextGetString(R.id.appEdtEmail)
            val password = appEdiTextGetString(R.id.appEdtPassword)
            val confirmPassword = appEdiTextGetString(R.id.appEdtConfirmPassword)
            val mobile =  appEdiTextGetString(R.id.appEdtMobile)

            if (validation(userName, email, password, confirmPassword, mobile)) {
                presenter!!.callApiPostSignUp(SignUpRequest(userName, email, password, null, mobile, null, null, null, null, null , 0, null));
            }
        })
    }

    override fun createPresenter(): SignUpPresenter {
        return SignUpPresenter()
    }

    override fun attachView(): SignUpView {
        return this
    }

    override fun onFailure(message: String) {
        AppUtils.showToast(this, message + "")
    }

    override fun apiGetCountryList(response: CountryResponse) {
        val dialogCountry = SpDialogCountry(this, getStringFromId(R.string.dialog_title_select_country), SpDialogCountry.OnItemClick { `object` ->
            val modelCountry = `object` as ModelCountry
            Logging.d(getToJsonClass(modelCountry))
        }, response.listCountry)
        dialogCountry.show()
    }

    override fun apiPostSignUp(response: SignUpResponse) {
        if (response.response!!.status == 200) {
            Preference.preferenceInstance(this).isLogin = true
            Preference.preferenceInstance(this).userId = response.signup!!.userid!!
            Preference.preferenceInstance(this).userName = response.signup!!.username!!
            Preference.preferenceInstance(this).email = response.signup!!.email!!

            AppUtils.startActivity(this, DrawerActivity::class.java)
            finish()
        } else if (response.response!!.status == RestConstant.conflict_409) {
            appEdiTextNullSet(R.id.appEdtPassword);
            appEdiTextNullSet(R.id.appEdtConfirmPassword);
        }

        AppUtils.showToast(this, response.response!!.message + "")

    }

    private fun validation(userName: String, email: String, password: String, confirmPassword: String, mobile: String): Boolean {
        return when {
            Validator.isEmpty(userName) -> {
                AppUtils.showToast(this, getString(R.string.msg_empty_username))
                return false
            }
            Validator.isEmpty(email) -> {
                AppUtils.showToast(this, getString(R.string.msg_empty_email))
                return false
            }
            Validator.isRegexValidator(email, Validator.patternEmail) === false -> {
                AppUtils.showToast(this, getString(R.string.msg_valid_email))
                return false
            }
            Validator.isEmpty(password) -> {
                AppUtils.showToast(this, getString(R.string.msg_empty_password))
                return false
            }
            Validator.isEmpty(confirmPassword) -> {
                AppUtils.showToast(this, getString(R.string.msg_empty_confirm_password))
                return false
            }
            Validator.isEqual(password, confirmPassword) === false -> {
                AppUtils.showToast(this, getString(R.string.msg_match_password))
                return false
            }
            Validator.isEmpty(mobile) -> {
                AppUtils.showToast(this, getString(R.string.msg_empty_mobile))
                return false
            }
            else -> return true
        }
    }

}