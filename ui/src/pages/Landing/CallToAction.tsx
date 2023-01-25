import React from 'react';


function CallToAction() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="cta-area section-padding-xl bg-red-150 wow fadeInUp animate__fast">
        <div className="container">
          <div className="row">
            <div className="col-12 text-center">
              <div className="section-title-center pb-8">
                <h2 className="mt-n3 mt-md-n4">Schedule a Demo</h2>
                <p>Let one of our team members show you how Kalico can give your video
                  content another life
                </p>
              </div>
              <form className="form-group mx-auto mt-0">
                <input type="email" placeholder="Enter your email to schedule demo"/>
                <button type="submit" className="btn btn-red">Schedule a Demo</button>
              </form>
            </div>
          </div>
        </div>
        <span className="shape7"></span>
        <img className="shape8 d-none d-lg-block" src="/assets/images/shape/shape-8.svg"
             alt="Shape"/>
      </section>
  );
}

export default CallToAction;
