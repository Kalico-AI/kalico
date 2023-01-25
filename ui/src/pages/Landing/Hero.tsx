import React from 'react';


function Hero() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="banner-area">
        <div className="container">
          <div className="row flex-column-reverse flex-md-row align-items-center">
            <div className="col-md-12">
              <div className="banner-left text-center">
                <h1>
                  Give your video content another life{' '}
                  <span>
                    with AI
                    <svg width="225" height="16" viewBox="0 0 225 16" fill="none"
                         xmlns="http://www.w3.org/2000/svg">
                      <path
                          d="M224.931 15.1987C151.063 1.40293 47.4825 6.23252 4.92601 10.3718L0.241161 6.21004C91.4615 -6.66766 188.043 6.83677 224.931 15.1987Z"
                          fill="#EC595A"
                      />
                    </svg>
                  </span>
                </h1>
                <p className="banner-para">Instantly create high quality blog posts and articles with Kalico from your videos</p>
                {/*<div className="form-group">*/}
                  {/*<input type="email" placeholder="Enter your email to schedule a demo"/>*/}
                  <button type="submit" className="btn btn-red">Create Account</button>
                {/*</div>*/}
                {/*<span className="sub-content">Free 14 days trial. <strong>No credit</strong> card required</span>*/}
              </div>
            </div>

            {/*<div className="col-md-4">*/}
            {/*  <div className="customer-video wow fadeInLeft">*/}
            {/*    <img className="customer-img" src="/assets/images/home_4/customer-video.png"*/}
            {/*         alt=""/>*/}
            {/*    <a className="play-btn" data-fancybox*/}
            {/*       href="https://www.youtube.com/watch?v=8Q1OPYfTJ1c">*/}
            {/*      <i className="fas fa-play"></i>*/}
            {/*    </a>*/}
            {/*  </div>*/}
            {/*</div>*/}

          </div>
        </div>
        {/* <img class="banner-shape-2" src="/assets/images/shape/bg-shape-2.png" alt="Pricing Banner Shape">*/}
      </section>
  );
}

export default Hero;
