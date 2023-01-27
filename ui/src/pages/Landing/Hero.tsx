import React from 'react';


function Hero() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="banner-area-4 pb-140">
        <div className="container">
          <div className="row flex-column-reverse flex-md-row align-items-center">
            <div className="col-md-12">
              <div className="banner-left text-center">
                <h2>
                  Give your video content another life{' '}
                  <span style={{color: '#ee8e3b'}}>
                    with AI
                  </span>
                </h2>
                <p className="banner-para">Instantly create high quality blog posts and articles from your videos</p>
                <div className="hero-cta">
                <button type="submit" className="btn btn-red">Get Started for Free</button>

                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default Hero;
