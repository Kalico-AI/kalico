import React, {FC, useEffect, useState} from 'react';
import {useStore} from "@/hooks/useStore";
import {UserApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {
  GoogleAuthProvider,
  indexedDBLocalPersistence, isSignInWithEmailLink, sendSignInLinkToEmail,
  setPersistence, signInWithEmailLink,
  signInWithPopup, TwitterAuthProvider
} from "firebase/auth";
import {auth} from "@/utils/firebase-setup";
import firebase from "firebase/compat";
import AuthProvider = firebase.auth.AuthProvider;
import {observer} from "mobx-react";
import {
  GoogleLoginButton,
  TwitterLoginButton
} from "react-social-login-buttons";
import {toast, ToastContainer} from "react-toastify";
import {registerUser} from "@/utils/utils";


const actionCodeSettings = {
  // URL you want to redirect back to. The domain (www.example.com) for this
  // URL must be in the authorized domains list in the Firebase Console.
  url: 'https://kalico.ai/account/sign-in-confirmation',
  // This must be true.
  handleCodeInApp: true
};

const SignInConfirmation: FC<any> = observer((props) => {
  const store = useStore()
  const [error, setError] = useState<string | undefined>(undefined)
  const [email, setEmail] = useState<string | undefined>(undefined)

  useEffect(() => {
    confirmEmail()
  }, [])

  useEffect(() => {
    if (error) {
      toast(error, {
        type: 'error',
        position: toast.POSITION.TOP_CENTER
      });
      setError(undefined)
    }
  }, [error])

  const handleEmail = (event: any): void => {
    event.preventDefault()
    setEmail(event.target.value)
  }

  const confirmEmail = (): void => {
    if (isSignInWithEmailLink(auth, window.location.href)) {
      // Additional state parameters can also be passed via URL.
      // This can be used to continue the user's intended action before triggering
      // the sign-in operation.
      // Get the email if available. This should be available if the user completes
      // the flow on the same device where they started it.
      let email = window.localStorage.getItem('emailForSignIn');
      if (email) {
        // The client SDK will parse the code from the link for you.
        signInWithEmailLink(auth, email, window.location.href)
        .then((result) => {
          // Clear email from storage.
          window.localStorage.removeItem('emailForSignIn');
          // You can access the new user via result.user
          // Additional user info profile not available via:
          // result.additionalUserInfo.profile == null
          // You can check if the user is new or existing:
          // result.additionalUserInfo.isNewUser
          registerUser(store.sessionDataStore)
        })
        .catch((error) => {
          console.log(error)
          setError(error)
          // Some error occurred, you can inspect the code: error.code
          // Common errors could be invalid email and invalid or expired OTPs.
        });
      }
    }
  }


  return (
    <>
      <section className="sign-up-wrapper overflow-hidden pt-215 pb-165">
        <div className="container">
          <div className="sign-up-box">
                <div className="form-wrapper form-wrapper-signin">
                  <div className="text-center">
                    <img className="sign-in-logo" src="/assets/images/logo.png" alt="logo"/>
                    <span className="sign-in-message">Please confirm your email address</span>
                  </div>
                      <form action="#" className="sign-up-form">
                    <div className="row">
                      <div className="col-md-12">
                        <input type="email" placeholder="Email" onChange={handleEmail}/>
                      </div>
                      <div className="col-md-12 mt-30">
                        <input className="btn btn-red" type="submit" value="Confirm" onClick={confirmEmail}/>
                      </div>
                    </div>
                  </form>
                </div>
          </div>
        </div>
      </section>
      <ToastContainer
          style={{width: '100%', maxWidth: '600px'}}
          position="top-center"
          autoClose={5000}
          hideProgressBar
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="colored"/>
    </>
  );
})

export default SignInConfirmation;

