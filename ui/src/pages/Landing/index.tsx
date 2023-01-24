import React from 'react';

function Landing() {
  return (
      <>
         {/* Preloader*/}

         {/* Header Area*/}

         {/* Header Area*/}
        <main>
           {/* Banner Area*/}
          <section className="banner-area">
            <div className="container">
              <div className="row flex-column-reverse flex-md-row align-items-center">
                <div className="col-md-6">
                  <div className="banner-left">
                    <h1>
                      Colaboration Without
                      <span>
                    Hassle
                    <svg width="225" height="16" viewBox="0 0 225 16" fill="none"
                         xmlns="http://www.w3.org/2000/svg">
                      <path
                          d="M224.931 15.1987C151.063 1.40293 47.4825 6.23252 4.92601 10.3718L0.241161 6.21004C91.4615 -6.66766 188.043 6.83677 224.931 15.1987Z"
                          fill="#EC595A"
                      />
                    </svg>
                  </span>
                    </h1>
                    <p className="banner-para">Diam et dolor interdum interdum faucibus et auctor. A
                      lectus tincidunt non molestie rhoncus at. Sed aliquam a neque.</p>
                    <form className="form-group">
                      <input type="email" placeholder="Enter your email to book demo"/>
                      <button type="submit" className="btn btn-red">Book A Demo</button>
                    </form>
                    <span className="sub-content">Free 14 days trial. <strong>No credit</strong> card required</span>
                  </div>
                </div>
                <div className="col-md-6">
                  <div className="banner-right">
                    <span className="banner-shapes wow fadeInDownBig"></span>
                    <img className="banner-shapes2 wow fadeInDown"
                         src="/assets/images/shape/banner-shape2.svg" alt="Shape Image"/>
                    <img className="banner-shapes3 wow fadeInDownBig" data-wow-duration="1.2s"
                         src="/assets/images/shape/banner-shape3.svg" alt="Shape Image"/>
                    <img className="banner-shapes4 wow fadeInLeft"
                         src="/assets/images/shape/banner-shape4.svg" alt="Shape Image"/>
                    <img className="banner-shapes5 wow fadeInRightBig"
                         src="/assets/images/shape/banner-shape5.svg" alt="Shape Image"/>
                    <img className="banner-shapes6 wow fadeInDown"
                         src="/assets/images/shape/banner-shape2.svg" alt="Shape Image"/>
                    <img className="banner-shapes7 wow fadeInRightBig"
                         src="/assets/images/shape/banner-shape6.svg" alt="Shape Image"/>
                  </div>
                </div>
              </div>
            </div>
             {/* <img class="banner-shape-2" src="/assets/images/shape/bg-shape-2.png" alt="Pricing Banner Shape">*/}
          </section>
           {/* Banner Area*/}

           {/* Client Area*/}
          <section className="client-area">
            <div className="container">
              <div className="client-wrapper">
                <div className="row">
                  <div className="col-12">
                    <div className="client-text">
                      <h4 className="mt-n3"><span>3000+ Companies</span> Trust Sturtaplanding to
                        build landing page for their <span>dream product</span></h4>
                    </div>
                  </div>
                </div>
                <div className="row justify-content-center">
                  <div className="w-50 w-md-20">
                    <div className="client-image">
                      <img src="/assets/images/brand/brand-1.svg" alt="Brand Image"/>
                    </div>
                  </div>
                  <div className="w-50 w-md-20">
                    <div className="client-image">
                      <img src="/assets/images/brand/brand-2.svg" alt="Brand Image"/>
                    </div>
                  </div>
                  <div className="w-50 w-md-20">
                    <div className="client-image">
                      <img src="/assets/images/brand/brand-3.svg" alt="Brand Image"/>
                    </div>
                  </div>
                  <div className="w-50 w-md-20">
                    <div className="client-image">
                      <img src="/assets/images/brand/brand-4.svg" alt="Brand Image"/>
                    </div>
                  </div>
                  <div className="w-50 w-md-20">
                    <div className="client-image">
                      <img src="/assets/images/brand/brand-5.svg" alt="Brand Image"/>
                    </div>
                  </div>
                </div>
                <div className="client-meta">
                  <ul className="client-review-icon">
                    <li><i className="fas fa-star"></i></li>
                    <li><i className="fas fa-star"></i></li>
                    <li><i className="fas fa-star"></i></li>
                    <li><i className="fas fa-star"></i></li>
                    <li><i className="fas fa-star"></i></li>
                  </ul>
                  <div className="client-review-number"><span>4.9</span> Stars</div>
                  <div className="client-review"><span>5121+</span> Reviews</div>
                </div>
              </div>
            </div>
          </section>
           {/* Client Area*/}

           {/* Features Area*/}
          <section className="features section-padding">
            <div className="container">
              <div className="row">
                <div className="col-lg-6">
                  <div className="features-image">
                    <img src="/assets/images/features/feature.png" alt="Features Image"/>
                    <img
                        src="/assets/images/features/features-object-1.svg"
                        alt="Features Object"
                        className="features-object1 object-element"
                        data-paroller-factor="0.1"
                        data-paroller-type="foreground"
                        data-paroller-direction="horizontal"
                        data-paroller-transition="transform .2s linear"
                    />
                    <img src="/assets/images/features/features-object-2.svg" alt="Features Object"
                         className="features-object2"/>
                    <img
                        className="features-object3 object-element"
                        data-paroller-factor="0.05"
                        data-paroller-type="foreground"
                        data-paroller-direction="horizontal"
                        data-paroller-transition="transform .2s linear"
                        src="/assets/images/features/features-object-3.svg"
                        alt="Features Object"
                    />
                    <img
                        src="/assets/images/features/features-object-4.svg"
                        alt="Features Object"
                        className="features-object4 object-element"
                        data-paroller-factor="0.05"
                        data-paroller-type="foreground"
                        data-paroller-transition="transform .2s linear"
                    />
                    <img
                        src="/assets/images/features/features-object-5.svg"
                        alt="Features Object"
                        className="features-object5 object-element"
                        data-paroller-type="foreground"
                        data-paroller-factor="0.1"
                        data-paroller-transition="transform .2s linear"
                    />
                    <div className="round-object"></div>
                  </div>
                </div>
                <div className="col-lg-6">
                  <div className="features-content">
                    <h2>Outstanding Features & Automations</h2>
                    <h3>
                      We created world's first project visualisation software for
                      <span>remote teams.</span>
                    </h3>
                    <p>
                      We're here every step of the way making sure you and your team deliver. We're
                      here every step of the way making sure you and your team deliver
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </section>
           {/* Features Area*/}

           {/* Why Choose us Area*/}
          <section className="why-choose section-padding">
            <div className="container">
              <div className="why-choose-wrapper">
                <div className="row">
                  <div className="col-md-4 wow fadeInUp animate__faster">
                    <div className="why-choose-item">
                      <div className="why-choose-icon">
                        <img src="/assets/images/icon/icon.svg" alt="Why Choose Icon"/>
                      </div>
                      <h4>Deadlines will never surprise you again.</h4>
                      <p>We're here every step of the way making sure you and your team deliver</p>
                    </div>
                  </div>
                  <div className="col-md-4 wow fadeInUp animate__fast">
                    <div className="why-choose-item">
                      <div className="why-choose-icon">
                        <img src="/assets/images/icon/icon-2.svg" alt="Why Choose Icon"/>
                      </div>
                      <h4>Everyone knows what need to do.</h4>
                      <p>We're here every step of the way making sure you and your team deliver</p>
                    </div>
                  </div>
                  <div className="col-md-4 wow fadeInUp">
                    <div className="why-choose-item">
                      <div className="why-choose-icon">
                        <img src="/assets/images/icon/icon-3.svg" alt="Why Choose Icon"/>
                      </div>
                      <h4>Less juggling, more time for Real Work.</h4>
                      <p>We're here every step of the way making sure you and your team deliver</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>
           {/* Why Choose us Area*/}

           {/* Features Area Two */}
          <section className="features-area-two section-padding">
            <div className="container">
              <div className="row align-items-center">
                <div className="col-md-6 col-lg-5">
                  <div className="features-content">
                    <h2 className="mt-n4">Fast & powerful to get out of your way</h2>
                    <p>Aenean amet netus aliquam elit eu, sagittis id natoque id.</p>
                    <div className="features-counter">
                      <span>Trusted By </span>
                      <br/>
                      <strong><span className="counter">3000</span>+</strong>
                      <br/>
                      <p>Company</p>
                    </div>
                  </div>
                </div>
                <div className="col-md-6 col-lg-7">
                  <div className="features-image">
                    <img src="/assets/images/features/features-2.png" alt="Features Image"/>
                  </div>
                </div>
              </div>
            </div>
          </section>
           {/* Features Area Two */}

           {/* Testimonial Area*/}
          {/*<section className="testimonial-area testimonial bg-black wow fadeInUp animate__fast">*/}
          {/*  <div id="fixedWrapper" className="pt-md-4">*/}
          {/*    <div className="container">*/}
          {/*      <div className="row">*/}
          {/*        <div className="col-12">*/}
          {/*          <div className="testimonial-title text-center">*/}
          {/*            <h2 className="text-white mt-n3">Choose a workflow or make your own</h2>*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*      </div>*/}
          {/*    </div>*/}
          {/*    <div className="container">*/}
          {/*      <div id="scroll-container">*/}
          {/*        <div className="row">*/}
          {/*          <div className="col-12 max-width">*/}
          {/*            <div className="testimonial-wrapper">*/}
          {/*              <div className="row align-items-center">*/}
          {/*                <div className="col-7 col-md-7">*/}
          {/*                  <div className="testimonial-content">*/}
          {/*                    <h3 className="mt-n4">Plan & Create</h3>*/}
          {/*                    <span> Elit sed sapien viverra eleifend malesuada purus pellentesque ut eget. </span>*/}
          {/*                    <p>Ligula ridiculus lacinia mattis non volutpat. Pellentesque id*/}
          {/*                      aenean diam aenean tincidunt non fermentum nunc mattis.</p>*/}
          {/*                    <div className="testimonial-review">*/}
          {/*                      <i className="fas fa-quote-left"></i>*/}
          {/*                      <p>*/}
          {/*                        Startup Landing is a great theme. Ligula ridiculus lacinia mattis*/}
          {/*                        non volutpat.*/}
          {/*                        <span>Ligula ridiculus lacinia</span>*/}
          {/*                      </p>*/}
          {/*                      <div className="testimonial-author">*/}
          {/*                        <div className="testimonial-author-image">*/}
          {/*                          <img src="/assets/images/testimonial/testimonial-1.jpg"*/}
          {/*                               alt="Testimonial"/>*/}
          {/*                        </div>*/}
          {/*                        <div className="testimonial-author-name">*/}
          {/*                          <h4>Jesus Requena</h4>*/}
          {/*                          <span>Support Engineer, Aliexpress</span>*/}
          {/*                        </div>*/}
          {/*                      </div>*/}
          {/*                    </div>*/}
          {/*                  </div>*/}
          {/*                </div>*/}
          {/*                <div className="col-5 col-md-5">*/}
          {/*                  <div className="testimonial-right-image">*/}
          {/*                    <img src="/assets/images/testimonial/testimonial.png" alt=""/>*/}
          {/*                  </div>*/}
          {/*                </div>*/}
          {/*              </div>*/}
          {/*            </div>*/}
          {/*          </div>*/}
          {/*          <div className="col-12 max-width">*/}
          {/*            <div className="testimonial-wrapper">*/}
          {/*              <div className="row align-items-center">*/}
          {/*                <div className="col-7 col-md-7">*/}
          {/*                  <div className="testimonial-content">*/}
          {/*                    <h3 className="mt-n4">Plan & Create</h3>*/}
          {/*                    <span>Elit sed sapien viverra eleifend malesuada purus pellentesque ut eget.</span>*/}
          {/*                    <p>Ligula ridiculus lacinia mattis non volutpat. Pellentesque id*/}
          {/*                      aenean diam aenean tincidunt non fermentum nunc mattis.</p>*/}
          {/*                    <div className="testimonial-review">*/}
          {/*                      <i className="fas fa-quote-left"></i>*/}
          {/*                      <p>*/}
          {/*                        Startup Landing is a great theme. Ligula ridiculus lacinia mattis*/}
          {/*                        non volutpat.*/}
          {/*                        <span>Ligula ridiculus lacinia</span>*/}
          {/*                      </p>*/}
          {/*                      <div className="testimonial-author">*/}
          {/*                        <div className="testimonial-author-image">*/}
          {/*                          <img src="/assets/images/testimonial/testimonial-1.jpg"*/}
          {/*                               alt="Testimonial"/>*/}
          {/*                        </div>*/}
          {/*                        <div className="testimonial-author-name">*/}
          {/*                          <h4>Jesus Requena</h4>*/}
          {/*                          <span>Support Engineer, Aliexpress</span>*/}
          {/*                        </div>*/}
          {/*                      </div>*/}
          {/*                    </div>*/}
          {/*                  </div>*/}
          {/*                </div>*/}
          {/*                <div className="col-5 col-md-5">*/}
          {/*                  <div className="testimonial-right-image">*/}
          {/*                    <img src="/assets/images/testimonial/testimonial.png" alt=""/>*/}
          {/*                  </div>*/}
          {/*                </div>*/}
          {/*              </div>*/}
          {/*            </div>*/}
          {/*          </div>*/}
          {/*          <div className="col-12 max-width">*/}
          {/*            <div className="testimonial-wrapper">*/}
          {/*              <div className="row align-items-center">*/}
          {/*                <div className="col-7 col-md-7">*/}
          {/*                  <div className="testimonial-content">*/}
          {/*                    <h3 className="mt-n4">Plan & Create</h3>*/}
          {/*                    <span>Elit sed sapien viverra eleifend malesuada purus pellentesque ut eget.</span>*/}
          {/*                    <p>Ligula ridiculus lacinia mattis non volutpat. Pellentesque id*/}
          {/*                      aenean diam aenean tincidunt non fermentum nunc mattis.</p>*/}
          {/*                    <div className="testimonial-review">*/}
          {/*                      <i className="fas fa-quote-left"></i>*/}
          {/*                      <p>*/}
          {/*                        Startup Landing is a great theme. Ligula ridiculus lacinia mattis*/}
          {/*                        non volutpat.*/}
          {/*                        <span>Ligula ridiculus lacinia</span>*/}
          {/*                      </p>*/}
          {/*                      <div className="testimonial-author">*/}
          {/*                        <div className="testimonial-author-image">*/}
          {/*                          <img src="/assets/images/testimonial/testimonial-1.jpg"*/}
          {/*                               alt="Testimonial"/>*/}
          {/*                        </div>*/}
          {/*                        <div className="testimonial-author-name">*/}
          {/*                          <h4>Jesus Requena</h4>*/}
          {/*                          <span>Support Engineer, Aliexpress</span>*/}
          {/*                        </div>*/}
          {/*                      </div>*/}
          {/*                    </div>*/}
          {/*                  </div>*/}
          {/*                </div>*/}
          {/*                <div className="col-5 col-md-5">*/}
          {/*                  <div className="testimonial-right-image">*/}
          {/*                    <img src="/assets/images/testimonial/testimonial.png" alt=""/>*/}
          {/*                  </div>*/}
          {/*                </div>*/}
          {/*              </div>*/}
          {/*            </div>*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*      </div>*/}
          {/*    </div>*/}
          {/*  </div>*/}
          {/*</section>*/}
           {/* Testimonial Area*/}

           {/* Features Area Three*/}
          <section className="features-area-three wow fadeInUp animate__fast" >
            <div className="container">
              <div className="row">
                <div className="col-12">
                  <div className="section-title-center">
                    <h2 className="mt-n4">A Workflow That Makes Sense</h2>
                    <p>Aenean amet netus aliquam elit eu, sagittis id natoque id. Purus augue
                      fermentum dui aliquam dui vel.</p>
                  </div>
                </div>
                <div className="col-12 wow fadeInUp">
                  <nav>
                    <div className="nav justify-content-center" id="nav-tab" role="tablist">
                      <button
                          className="nav-link active"
                          id="nav-carban-tab"
                          data-bs-toggle="tab"
                          data-bs-target="#nav-carban"
                          type="button"
                          role="tab"
                          aria-controls="nav-carban"
                          aria-selected="true"
                      >
                        Carban
                      </button>
                      <button
                          className="nav-link"
                          id="nav-timeline-tab"
                          data-bs-toggle="tab"
                          data-bs-target="#nav-timeline"
                          type="button"
                          role="tab"
                          aria-controls="nav-timeline"
                          aria-selected="false"
                      >
                        Timeline
                      </button>
                      <button
                          className="nav-link"
                          id="nav-list-tab"
                          data-bs-toggle="tab"
                          data-bs-target="#nav-list"
                          type="button"
                          role="tab"
                          aria-controls="nav-list"
                          aria-selected="false"
                      >
                        List
                      </button>
                      <button
                          className="nav-link"
                          id="nav-calendar-tab"
                          data-bs-toggle="tab"
                          data-bs-target="#nav-calendar"
                          type="button"
                          role="tab"
                          aria-controls="nav-calendar"
                          aria-selected="false"
                      >
                        Calendar
                      </button>
                    </div>
                  </nav>
                  <div className="tab-content features-tab-content" id="nav-tabContent">
                    <div className="tab-pane fade show active" id="nav-carban" role="tabpanel"
                         aria-labelledby="nav-carban-tab">
                      <div className="features-tab-image">
                        <img src="/assets/images/features/features-3.png" alt="Features"/>
                      </div>
                    </div>
                    <div className="tab-pane fade" id="nav-timeline" role="tabpanel"
                         aria-labelledby="nav-timeline-tab">
                      <div className="features-tab-image">
                        <img src="/assets/images/features/features-3.png" alt="Features"/>
                      </div>
                    </div>
                    <div className="tab-pane fade" id="nav-calendar" role="tabpanel"
                         aria-labelledby="nav-calendar-tab">
                      <div className="features-tab-image">
                        <img src="/assets/images/features/features-3.png" alt="Features"/>
                      </div>
                    </div>
                    <div className="tab-pane fade" id="nav-list" role="tabpanel"
                         aria-labelledby="nav-list-tab">
                      <div className="features-tab-image">
                        <img src="/assets/images/features/features-3.png" alt="Features"/>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="col-12">
                  <div className="features-form text-center">
                    <form className="form-group">
                      <input type="email" placeholder="Enter your email to book demo"/>
                      <button type="submit" className="btn btn-red">Book A Demo</button>
                    </form>
                    <span className="sub-content">Free 14 days trial. No credit card required</span>
                  </div>
                </div>
              </div>
            </div>
          </section>
           {/* Features Area Three*/}

           {/* Features Area Four*/}
          <section
              className="features features-area-four section-padding wow fadeInUp animate__fast">
            <div className="container">
              <div className="row align-items-center flex-column-reverse flex-lg-row">
                <div className="col-lg-6">
                  <div className="features-content features-content-four">
                    <h2>Realtime insight about your team</h2>
                    <h3>Designed following <span>convertion</span> metrix</h3>
                    <p>
                      We're here every step of the way making sure you and your team deliver. We're
                      here every step of the way making sure you and your team deliver
                    </p>
                  </div>
                </div>
                <div className="col-lg-6">
                  <div className="features-image">
                    <img src="/assets/images/features/features-4.png" alt="Features Image"/>
                    <img src="/assets/images/features/features-object-6.svg" alt="Features Object"
                         className="features-object6"/>
                    <img
                        src="/assets/images/features/features-object-7.svg"
                        alt="Features Object"
                        className="features-object7 object-element"
                        data-paroller-factor="0.05"
                        data-paroller-type="foreground"
                        data-paroller-direction="horizontal"
                        data-paroller-transition="transform .2s linear"
                    />
                    <img
                        src="/assets/images/features/features-object-8.svg"
                        alt="Features Object"
                        className="features-object8 object-element"
                        data-paroller-factor="0.05"
                        data-paroller-type="foreground"
                        data-paroller-transition="transform .2s linear"
                    />
                    <img
                        src="/assets/images/features/features-object-9.svg"
                        alt="Features Object"
                        className="features-object9 object-element"
                        data-paroller-factor="-0.05"
                        data-paroller-type="foreground"
                        data-paroller-transition="transform .2s linear"
                    />
                  </div>
                </div>
              </div>
            </div>
          </section>
           {/* Features Area Four*/}

           {/* Features Area Five*/}
          <section className="features-area-five section-padding wow fadeInUp animate__fast">
            <div className="container">
              <div className="row">
                <div className="col-12">
                  <div className="section-title-center">
                    <h2 className="mt-n4">Move work forward from anywhere</h2>
                  </div>
                  <div className="feature-image">
                    <img src="/assets/images/features/feature-5.png" alt="Features Image"/>
                    <img src="/assets/images/features/features-8.png" alt="Features Image"
                         className="features8"/>
                    <img src="/assets/images/features/features-9.png" alt="Features Image"
                         className="features9"/>
                    <p>
                      We're here every step of the way making sure you and your team deliver. We're
                      here every step of the way making sure you and your team deliver
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </section>
           {/* Features Area Five*/}

           {/* Logo Grid Area*/}
          <section className="logo-grid-area wow fadeInUp animate__fast">
            <div className="container-fluid">
              <div className="logo-grid-wrapper">
                <div className="row flex-column-reverse flex-lg-row align-items-center">
                  <div className="col-lg-6">
                    <div className="section-title-left">
                      <h2 className="mt-n3">Connected to the tools you love</h2>
                      <span>Easy way to integrate</span>
                      <p>
                        We're here every step of the way making sure you and your team deliver.
                        We're here every step of the way making sure you and your team
                        deliver
                      </p>
                    </div>
                  </div>
                  <div className="col-lg-6">
                    <div className="logo-grid">
                      <div className="row no-gutters">
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo-2.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo-3.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo-4.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo-5.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo-2.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo-3.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo-4.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo-2.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item">
                            <img src="/assets/images/logo/logo-5.png" alt="Logo grid Image"/>
                          </div>
                        </div>
                        <div className="col-4 col-sm-3 mb-4">
                          <div className="logo-grid-item logo-grid-text">
                            <h3 className="text-red m-auto">+30</h3>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>
           {/* Logo Grid Area*/}

           {/* Testimonial Area*/}
          {/*<section*/}
          {/*    className="testimonial-area testimonial-area-two bg-white-800 wow fadeInUp animate__fast">*/}
          {/*  <div id="testimonial-2" className="swiper-container">*/}
          {/*    <div className="swiper-wrapper">*/}
          {/*      <div className="swiper-slide">*/}
          {/*        <div className="testimonial-review">*/}
          {/*          <i className="fas fa-quote-left"></i>*/}
          {/*          <p>*/}
          {/*            Continually harness functional e-business without global core competencies.*/}
          {/*            Interactively pontificate client-focused web-readiness via quality*/}
          {/*            scenarios. Professionally pursue collaborative*/}
          {/*            <span>ideas rather than prospective imperatives.</span>*/}
          {/*          </p>*/}
          {/*          <div className="testimonial-author">*/}
          {/*            <div className="testimonial-author-image">*/}
          {/*              <img src="/assets/images/testimonial/testimonial-1.jpg" alt="Testimonial"/>*/}
          {/*            </div>*/}
          {/*            <div className="testimonial-author-name">*/}
          {/*              <h4>Jesus Requena</h4>*/}
          {/*              <span>Support Engineer, Aliexpress</span>*/}
          {/*            </div>*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*      </div>*/}
          {/*      <div className="swiper-slide">*/}
          {/*        <div className="testimonial-review">*/}
          {/*          <i className="fas fa-quote-left"></i>*/}
          {/*          <p>*/}
          {/*            Continually harness functional e-business without global core competencies.*/}
          {/*            Interactively pontificate client-focused web-readiness via quality*/}
          {/*            scenarios. Professionally pursue collaborative*/}
          {/*            <span>ideas rather than prospective imperatives.</span>*/}
          {/*          </p>*/}
          {/*          <div className="testimonial-author">*/}
          {/*            <div className="testimonial-author-image">*/}
          {/*              <img src="/assets/images/testimonial/testimonial-1.jpg" alt="Testimonial"/>*/}
          {/*            </div>*/}
          {/*            <div className="testimonial-author-name">*/}
          {/*              <h4>Jesus Requena</h4>*/}
          {/*              <span>Support Engineer, Aliexpress</span>*/}
          {/*            </div>*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*      </div>*/}
          {/*      <div className="swiper-slide">*/}
          {/*        <div className="testimonial-review">*/}
          {/*          <i className="fas fa-quote-left"></i>*/}
          {/*          <p>*/}
          {/*            Continually harness functional e-business without global core competencies.*/}
          {/*            Interactively pontificate client-focused web-readiness via quality*/}
          {/*            scenarios. Professionally pursue collaborative*/}
          {/*            <span>ideas rather than prospective imperatives.</span>*/}
          {/*          </p>*/}
          {/*          <div className="testimonial-author">*/}
          {/*            <div className="testimonial-author-image">*/}
          {/*              <img src="/assets/images/testimonial/testimonial-1.jpg" alt="Testimonial"/>*/}
          {/*            </div>*/}
          {/*            <div className="testimonial-author-name">*/}
          {/*              <h4>Jesus Requena</h4>*/}
          {/*              <span>Support Engineer, Aliexpress</span>*/}
          {/*            </div>*/}
          {/*          </div>*/}
          {/*        </div>*/}
          {/*      </div>*/}
          {/*    </div>*/}
          {/*  </div>*/}
          {/*</section>*/}
           {/* Testimonial Area*/}

           {/* FAQs Area*/}
          <section className="faq-area wow fadeInUp animate__fast">
            <div className="container">
              <div className="row">
                <div className="col-12">
                  <div className="section-title-center">
                    <h2 className="font-bold mt-n3 mt-md-n4">Frequently asked questions</h2>
                    <p>Aenean amet netus aliquam elit eu, sagittis id natoque id. Purus augue
                      fermentum dui aliquam dui vel.</p>
                  </div>
                </div>
                <div className="col-12">
                  <div className="accordion" id="projectAccordion">
                    <div className="accordion-item">
                      <h2 className="accordion-header" id="project-headingOne">
                        <button
                            className="accordion-button collapsed"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#project-collapseOne"
                            aria-expanded="false"
                            aria-controls="project-collapseOne"
                        >
                          <span>Q1.</span> How can i use Landpagy?
                        </button>
                      </h2>
                      <div id="project-collapseOne" className="accordion-collapse collapse"
                           aria-labelledby="project-headingOne" data-bs-parent="#projectAccordion">
                        <div className="accordion-body">
                          <p>
                            Follow these 6 steps and you’ll get your Help Scout account up and
                            running in no time. If you'd like to get a full tour of Help Scout
                            and all its features, attend one of our weekly live demos or take a
                            video tour.
                          </p>
                        </div>
                      </div>
                    </div>
                    <div className="accordion-item">
                      <h2 className="accordion-header" id="project-headingTwo">
                        <button
                            className="accordion-button collapsed"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#project-collapseTwo"
                            aria-expanded="false"
                            aria-controls="project-collapseTwo"
                        >
                          <span>Q2.</span> When should I use a Tag?
                        </button>
                      </h2>
                      <div id="project-collapseTwo" className="accordion-collapse collapse"
                           aria-labelledby="project-headingTwo" data-bs-parent="#projectAccordion">
                        <div className="accordion-body">
                          <p>
                            Follow these 6 steps and you’ll get your Help Scout account up and
                            running in no time. If you'd like to get a full tour of Help Scout
                            and all its features, attend one of our weekly live demos or take a
                            video tour.
                          </p>
                        </div>
                      </div>
                    </div>
                    <div className="accordion-item">
                      <h2 className="accordion-header" id="project-headingThree">
                        <button
                            className="accordion-button collapsed"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#project-collapseThree"
                            aria-expanded="false"
                            aria-controls="project-collapseThree"
                        >
                          <span>Q3.</span> How can I make all this less work?
                        </button>
                      </h2>
                      <div id="project-collapseThree" className="accordion-collapse collapse"
                           aria-labelledby="project-headingThree"
                           data-bs-parent="#projectAccordion">
                        <div className="accordion-body">
                          <p>
                            Follow these 6 steps and you’ll get your Help Scout account up and
                            running in no time. If you'd like to get a full tour of Help Scout
                            and all its features, attend one of our weekly live demos or take a
                            video tour.
                          </p>
                        </div>
                      </div>
                    </div>
                    <div className="accordion-item">
                      <h2 className="accordion-header" id="project-headingFour">
                        <button
                            className="accordion-button collapsed"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#project-collapseFour"
                            aria-expanded="false"
                            aria-controls="project-collapseFour"
                        >
                          <span>Q3.</span> What’s up, Docs?
                        </button>
                      </h2>
                      <div id="project-collapseFour" className="accordion-collapse collapse"
                           aria-labelledby="project-collapseFour"
                           data-bs-parent="#projectAccordion">
                        <div className="accordion-body">
                          <p>
                            Follow these 6 steps and you’ll get your Help Scout account up and
                            running in no time. If you'd like to get a full tour of Help Scout
                            and all its features, attend one of our weekly live demos or take a
                            video tour.
                          </p>
                        </div>
                      </div>
                    </div>
                    <div className="accordion-item">
                      <h2 className="accordion-header" id="project-headingFive">
                        <button
                            className="accordion-button collapsed"
                            type="button"
                            data-bs-toggle="collapse"
                            data-bs-target="#project-collapseFive"
                            aria-expanded="false"
                            aria-controls="project-collapseFive"
                        >
                          <span>Q3.</span> What’s up, Docs?
                        </button>
                      </h2>
                      <div id="project-collapseFive" className="accordion-collapse collapse"
                           aria-labelledby="project-collapseFive"
                           data-bs-parent="#projectAccordion">
                        <div className="accordion-body">
                          <p>
                            Follow these 6 steps and you’ll get your Help Scout account up and
                            running in no time. If you'd like to get a full tour of Help Scout
                            and all its features, attend one of our weekly live demos or take a
                            video tour.
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>
           {/* FAQs Area*/}

           {/* CTA Area*/}
          <section className="cta-area section-padding-xl bg-red-150 wow fadeInUp animate__fast">
            <div className="container">
              <div className="row">
                <div className="col-12 text-center">
                  <div className="section-title-center pb-8">
                    <h2 className="mt-n3 mt-md-n4">Get free for 14 Days</h2>
                    <p>Aenean amet netus aliquam elit eu, sagittis id natoque id. Purus augue
                      fermentum dui aliquam dui vel.</p>
                  </div>
                  <form className="form-group mx-auto mt-0">
                    <input type="email" placeholder="Enter your email to book demo"/>
                    <button type="submit" className="btn btn-red">Book A Demo</button>
                  </form>
                </div>
              </div>
            </div>
            <span className="shape7"></span>
            <img className="shape8 d-none d-lg-block" src="/assets/images/shape/shape-8.svg"
                 alt="Shape"/>
          </section>
           {/* CTA Area*/}
        </main>

         {/*Scroll to top */}
        <a href="#" className="scrollToTop">
          <img src="/assets/images/scroll.svg" alt=""/>
        </a>
      </>
  );
}

export default Landing;
