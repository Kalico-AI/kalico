import React, {FC} from 'react';
import {useStore} from "@/hooks/useStore";
import {UserApi} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import {
  FacebookAuthProvider,
  GoogleAuthProvider,
  indexedDBLocalPersistence, OAuthProvider,
  setPersistence,
  signInWithPopup, TwitterAuthProvider
} from "firebase/auth";
import {auth} from "@/utils/firebase-setup";
import {Button} from "@mui/material";
import firebase from "firebase/compat";
import AuthProvider = firebase.auth.AuthProvider;
import {observer} from "mobx-react";

export interface SignInProps {
  isSignUp: boolean
}

const SignIn: FC<SignInProps> = observer((props) => {
  const store = useStore()

  /**
   * Sign in user using Google login pop screen. After login, persist the user
   * object in indexDB so that the session is not terminated if the user refreshes
   * the page or closes the browser.
   */
  const firebaseSignIn = (provider: AuthProvider) => {
    setPersistence(auth, indexedDBLocalPersistence)
    .then(() => {
      // In local persistence will be applied to the signed in Google user
      return signInWithPopup(auth, provider)
      .then((result) => {
        if (result.user) {
          registerUser()
        } else {
          // showMessageBar({
          //   message: "Not Authorized",
          //   snack: enqueueSnackbar,
          //   error: true
          // });
        }
      }).catch(e => console.log(e));
    })
    .catch((error) => {
      // showMessageBar({
      //   message: error.message,
      //   snack: enqueueSnackbar,
      //   error: true
      // });
    });
  }

  /**
   * Check if the current user is registered. If not, register them. Once
   * registration has been verified or completed, disable the progress indicator and
   * route the user to the dashboard.
   *
   */
  const registerUser = () => {
    auth.onAuthStateChanged(user => {
      if (user) {
        user.getIdTokenResult(false)
        .then(tokenResult => {
          new UserApi(headerConfig(tokenResult.token))
          .getUserprofile()
          .then(result => {
            if (result.data.profile) {
              store.sessionDataStore.setUser(result.data.profile)
            } else {
              // showMessageBar({
              //   message: result.data.error,
              //   snack: enqueueSnackbar,
              //   error: true
              // });
            }
          }).catch(e => console.log(e))
        }).catch(e => console.log(e))
      }
    })
  }


  return (
    <>
      <section className="sign-up-wrapper overflow-hidden pt-215 pb-165">
        <div className="container">
          <div className="sign-up-box">
                <div className="form-wrapper form-wrapper-signin">
                  <div className="text-center">
                    <img className="sign-in-logo" src="/assets/images/logo.png" alt="logo"/>
                    <ul className="create-with-list  mb-35">
                      <li>
                        <Button
                            className="sign-in-button-icon"
                            size='medium'
                            variant='text'
                            onClick={() => firebaseSignIn(new GoogleAuthProvider())}
                        >
                          <img src="/assets/images/signup/icon1.svg" alt="Icon"/>
                        </Button>
                      </li>
                      <li>
                        <Button
                            className="sign-in-button-icon"
                            size='medium'
                            variant='text'
                            onClick={() => firebaseSignIn(new OAuthProvider('apple.com'))}
                        >
                          <img src="/assets/images/signup/icons8-apple-logo.svg" alt="Icon"/>
                        </Button>
                      </li>
                      <li>
                        <Button
                            className="sign-in-button-icon"
                            size='medium'
                            variant='text'
                            onClick={() => firebaseSignIn(new TwitterAuthProvider())}
                        >
                          <img src="/assets/images/signup/icon2.svg" alt="Icon"/>
                        </Button>
                      </li>
                      <li>
                        <Button
                            className="sign-in-button-icon"
                            size='medium'
                            variant='text'
                            onClick={() => firebaseSignIn(new FacebookAuthProvider())}
                        >
                          <img src="/assets/images/signup/icons8-facebook.svg" alt="Icon"/>
                        </Button>
                      </li>
                    </ul>

                    <span className="divider">or</span>
                  </div>

                  {props.isSignUp ? (
                          <form action="#" className="sign-up-form">
                            <div className="row">
                              <div className="col-md-6">
                                <input type="text" placeholder="First Name*"/>
                              </div>
                              <div className="col-md-6 mt-4 mt-md-0">
                                <input type="text" placeholder="Last Name*"/>
                              </div>
                              <div className="col-md-12 mt-20">
                                <input type="email" placeholder="Email address*"/>
                              </div>
                              <div className="col-md-12 mt-20">
                                <input type="password" placeholder="Password*"/>
                              </div>
                              <div className="col-md-12 mt-10">
                                <p className="meta-text">Make sure it's at least 15 characters OR at least 8
                                  characters including a number and a lowercase letter.</p>
                              </div>
                              {/*<div className="col-md-12">*/}
                              {/*  <input type="checkbox" id="remember"/>*/}
                              {/*  <label className="check-text" htmlFor="remember"> Remember me </label>*/}
                              {/*</div>*/}
                              {/*<div className="col-md-12">*/}
                              {/*  <input type="checkbox" id="news"/>*/}
                              {/*  <label className="check-text" htmlFor="news"> I’d like being informed about*/}
                              {/*    latest news and tips </label>*/}
                              {/*</div>*/}
                              <div className="col-md-12 mt-30">
                                <input className="btn btn-red" type="submit" value="Sign Up"/>
                              </div>

                              <div className="col-md-12">
                                <p className="form-text mt-20">
                                  Already have an account? {' '}
                                  <a href="/account/sign-in">Sign in</a>
                                </p>
                                <p className="form-text mt-15">
                                  By clicking “Sign up” you agree to our {' '}
                                  <a href="#">Terms of Service.</a>
                                </p>
                              </div>
                            </div>
                          </form>
                      ) :
                  (<form action="#" className="sign-up-form">
                    <div className="row">
                      <div className="col-md-12">
                        <input type="email" placeholder="Email address or username"/>
                      </div>
                      <div className="col-md-12 mt-20">
                        <input type="password" placeholder="Password*"/>
                      </div>
                      <div className="col-6">
                        <input type="checkbox" id="remember"/>
                        <label className="check-text" htmlFor="remember"> Remember me </label>
                      </div>
                      <div className="col-6">
                        <a className="forgot-link float-end" href="#">Forgot Password?</a>
                      </div>
                      <div className="col-md-12 mt-30">
                        <input className="btn btn-red" type="submit" value="Sign In"/>
                      </div>

                      <div className="col-md-12">
                        <p className="form-text mt-20">
                          Don’t have an account?
                          <br/>
                          <a href="/account/sign-up">Create account</a>
                        </p>
                      </div>
                    </div>
                  </form>)
                  }
                </div>
          </div>
        </div>
      </section>

    </>
  );
})

export default SignIn;

