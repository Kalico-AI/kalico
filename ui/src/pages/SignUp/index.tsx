import React from 'react';
import Head from "next/head";

export async function getServerSideProps() {
  return {
    props: {
      title: "Kalico",
      description: "",
      siteImage: "https://"
    }
  }
}

function SignUp(props) {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
    <>
      <Head>
        <title>{props.title}</title>
        <meta property="og:title" content={props.title} name="title" key="title"/>
        <meta property="og:description" content={props.description} name="description" key="description"/>
        <meta property="og:image:secure" content={props.siteImage} name="image" key="image:secure"/>
      </Head>
      <section className="sign-up-wrapper overflow-hidden pt-215 pb-165">
        <div className="container">
          <div className="sign-up-box">
            <div className="bg-shapes">
              <div className="shape">
                <img src="/assets/images/signup/shape2.svg" alt="Shape"/>
              </div>
              <div className="shape" data-parallax='{"x":-30, "y": 0, "rotateY":0}'>
                <img src="/assets/images/signup/shape3.svg" alt="Shape"/>
              </div>
              <div className="shape">
                <img src="/assets/images/signup/shape4.svg" alt="Shape"/>
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6">
                <div className="form-images d-lg-block d-none">
                  <div
                      className="shapes object-element"
                      data-paroller-factor="0.1"
                      data-paroller-type="foreground"
                      data-paroller-direction="horizontal"
                      data-paroller-transition="transform .2s linear"
                  ></div>
                  <div className="shapes object-element"></div>
                  <div
                      className="shapes object-element"
                      data-paroller-factor="0.05"
                      data-paroller-type="foreground"
                      data-paroller-direction="horizontal"
                      data-paroller-transition="transform .2s linear"
                  ></div>
                  <div
                      className="shapes object-element"
                      data-paroller-factor="0.05"
                      data-paroller-type="foreground"
                      data-paroller-transition="transform .2s linear"
                  ></div>
                  <div
                      className="shapes object-element"
                      data-paroller-type="foreground"
                      data-paroller-factor="0.1"
                      data-paroller-transition="transform .2s linear"
                  ></div>
                  <div className="shapes">
                    <img src="/assets/images/signup/shape1.svg" alt="Shape"/>
                  </div>
                </div>
              </div>

              <div className="col-lg-6">
                <div className="form-wrapper">
                  <div className="text-center">
                    <h2 className="heading-3 mb-15">Create your account</h2>
                    <p className="heading-text">Redownload themes, get support, and review
                      themes.</p>

                    <ul className="create-with-list mt-45 mb-35">
                      <li>
                        <a href="#"><img src="/assets/images/signup/icon1.svg" alt="Icon"/></a>
                      </li>
                      <li>
                        <a href="#"><img src="/assets/images/signup/icon2.svg" alt="Icon"/></a>
                      </li>
                      <li>
                        <a href="#"><img src="/assets/images/signup/icon3.svg" alt="Icon"/></a>
                      </li>
                      <li>
                        <a href="#"><img src="/assets/images/signup/icon4.svg" alt="Icon"/></a>
                      </li>
                    </ul>

                    <span className="divider">or</span>
                  </div>

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
                      <div className="col-md-12">
                        <input type="checkbox" id="remember"/>
                        <label className="check-text" htmlFor="remember"> Remember me </label>
                      </div>
                      <div className="col-md-12">
                        <input type="checkbox" id="news"/>
                        <label className="check-text" htmlFor="news"> I’d like being informed about
                          latest news and tips </label>
                      </div>
                      <div className="col-md-12 mt-30">
                        <input className="btn btn-red" type="submit" value="Sign Up"/>
                      </div>

                      <div className="col-md-12">
                        <p className="form-text mt-20">
                          Already have an account?
                          <a href="/account/sign-in">Sign in</a>
                        </p>
                        <p className="form-text mt-15">
                          By clicking “Sign up” you agree to our
                          <a href="#">Terms of Service.</a>
                        </p>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

    </>
  );
}

export default SignUp;
