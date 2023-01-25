import React from 'react';


function HowItWorks() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <>
        <section className="process-area-2 overflow-hidden pt-55">
          <div className="container" id="how-it-works">
            <div className="row">
              <div className="col-12">
                <h2 className="heading-3 text-center mb-90 wow fadeInUp"
                    data-wow-delay="0.1s">How Kalico Works</h2>
              </div>
            </div>

            <div className="row">
              <div className="col-lg-4 col-md-6">
                <div className="process-item-2 wow fadeInLeft" data-wow-delay="0.1s">
                  <img className="line-1" src="/assets/images/process/shape11.svg" alt="Shape"/>
                  <img className="line-2" src="/assets/images/process/shape12.svg" alt="Shape"/>
                  <span className="item-number">1</span>
                  <h3 className="item-title">Submit your video link</h3>
                  <p className="item-text">Submit a link to Instagram Reel or a YouTube video. The video content must
                  have audio or text in the description</p>
                </div>
              </div>
              <div className="col-lg-4 col-md-6 mt-md-0 mt-5">
                <div className="process-item-2 wow fadeInLeft" data-wow-delay="0.3s">
                  <img className="line-3" src="/assets/images/process/shape13.svg" alt="Shape"/>
                  <span className="item-number">2</span>
                  <h3 className="item-title">AI transforms content</h3>
                  <p className="item-text">Our AI transforms the video content to text and images. It then generates a fully-formed
                  article or blog post base on the template selected. The whole process takes a few minutes.</p>
                </div>
              </div>
              <div className="col-lg-4 col-md-6 mt-lg-0 mt-5 mx-auto">
                <div className="process-item-2 wow fadeInLeft" data-wow-delay="0.5s">
                  <img className="line-4" src="/assets/images/process/shape14.svg" alt="Shape"/>
                  <span className="item-number">3</span>
                  <h3 className="item-title">Review and Export</h3>
                  <p className="item-text">
                    Review the output and export a CSV or JSON file to use in your blogging platform such as WordPress.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </section>
      {/*<section className="deal-fast-area pt-140 pb-140">*/}
      {/*  <div className="container">*/}
      {/*    <div className="section-title-center">*/}
      {/*      <h2 className="wow fadeInUp">Make more deals faster</h2>*/}
      {/*      <p className="wow fadeInUp" data-wow-delay="0.2s">*/}
      {/*        Over a dozen reusable components built to provide iconography, dropdowns, input*/}
      {/*        groups, navigation, alerts, and much more.*/}
      {/*      </p>*/}
      {/*    </div>*/}
      {/*    <div className="row gy-4 gy-lg-0">*/}
      {/*      <div className="col-lg-5">*/}
      {/*        <div className="info-card me-lg-5 nav-tabs nav automated-tab" role="tablist">*/}
      {/*          <a className="nav-link" aria-selected="false" role="tab" data-bs-toggle="tab"*/}
      {/*             data-bs-target="#payment-track-one" data-wow-delay="0.1s">*/}
      {/*            <div className="icon">*/}
      {/*              <img src="/assets/images/home_3/fast-deal-1.svg" alt="icon"/>*/}
      {/*            </div>*/}
      {/*            <div className="info-txt">*/}
      {/*              <h6>Track key events</h6>*/}
      {/*              <p>Set up key event to track onboarding are maintains here</p>*/}
      {/*            </div>*/}
      {/*            <div className="progress-bar"></div>*/}
      {/*          </a>*/}
      {/*          <a className="nav-link" aria-selected="false" role="tab" data-bs-toggle="tab"*/}
      {/*             data-bs-target="#payment-track-two" data-wow-delay="0.3s">*/}
      {/*            <div className="icon">*/}
      {/*              <img src="/assets/images/home_3/fast-deal-2.svg" alt="icon"/>*/}
      {/*            </div>*/}
      {/*            <div className="info-txt">*/}
      {/*              <h6>Track key events</h6>*/}
      {/*              <p>Set up key event to track onboarding are maintains here</p>*/}
      {/*            </div>*/}
      {/*            <div className="progress-bar"></div>*/}
      {/*          </a>*/}
      {/*          <a aria-selected="false" role="tab" data-bs-toggle="tab"*/}
      {/*             data-bs-target="#payment-track-three" className="nav-link" data-wow-delay="0.5s">*/}
      {/*            <div className="icon">*/}
      {/*              <img src="/assets/images/home_3/fast-deal-3.svg" alt="icon"/>*/}
      {/*            </div>*/}
      {/*            <div className="info-txt">*/}
      {/*              <h6>Track key events</h6>*/}
      {/*              <p>Set up key event to track onboarding are maintains here</p>*/}
      {/*            </div>*/}
      {/*            <div className="progress-bar"></div>*/}
      {/*          </a>*/}
      {/*        </div>*/}
      {/*      </div>*/}
      {/*      <div className="col-lg-7 ps-lg-0">*/}
      {/*        <div className="tab-content">*/}
      {/*          <div className="tab-pane fade" id="payment-track-one" role="tabpanel">*/}
      {/*            <div className="main-img" data-wow-delay="0.1s">*/}
      {/*              <img src="/assets/images/home_3/fast-deal-4.png" alt=""/>*/}
      {/*              <img src="/assets/images/home_3/fast-deal-5.png" alt=""/>*/}
      {/*            </div>*/}
      {/*          </div>*/}
      {/*          <div className="tab-pane fade" id="payment-track-two" role="tabpanel">*/}
      {/*            <div className="main-img" data-wow-delay="0.1s">*/}
      {/*              <img src="/assets/images/home_3/fast-deal-6.png" alt=""/>*/}
      {/*              <img src="/assets/images/home_3/fast-deal-7.png" alt=""/>*/}
      {/*            </div>*/}
      {/*          </div>*/}
      {/*          <div className="tab-pane fade" id="payment-track-three" role="tabpanel">*/}
      {/*            <div className="main-img" data-wow-delay="0.1s">*/}
      {/*              <img src="/assets/images/home_3/fast-deal-4.png" alt=""/>*/}
      {/*              <img src="/assets/images/home_3/fast-deal-5.png" alt=""/>*/}
      {/*            </div>*/}
      {/*          </div>*/}
      {/*        </div>*/}
      {/*      </div>*/}
      {/*    </div>*/}
      {/*  </div>*/}
      {/*</section>*/}
        </>
  );
}

export default HowItWorks;
