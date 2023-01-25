import React from 'react';


function CallToAction() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="cta-area-five pt-100 pb-100 wow fadeInUp">
        <div className="container">
          <div className="cta-box">
            <div className="bg-shapes">
              <div className="shape">
                <img src="/assets/images/home_4/test-shape3.svg" alt=""/>
              </div>
              <div className="shape">
                <img src="/assets/images/home_4/cta-shape.svg" alt=""/>
              </div>
              <div className="shape">
                <img src="/assets/images/home_4/test-shape2.svg" alt=""/>
              </div>
            </div>
            <div className="row align-items-center">
              <div className="col-lg-5 col-md-6">
                <h2 className="section-title">Try Kalico for free today </h2>
              </div>
              <div className="col-md-6">
                <div className="form-group mt-md-n4">
                  <button type="submit" className="btn btn-red">Create Account</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      // <section className="cta-area-four pt-105 pb-105 wow fadeInUp animate__fast">
      //   <div className="bg-shape">
      //     <div className="shape" data-parallax='{"x": 0, "y": 40, "rotateY": 0}'></div>
      //     <div className="shape" data-parallax='{"x": 0, "y": 0, "rotateX": 0}'></div>
      //     <div className="shape" data-parallax='{"x": 0, "y": 0, "rotateZ": 200}'></div>
      //     <div className="shape" data-parallax='{"x": 0, "y": 0, "rotateZ": 100}'></div>
      //     <div className="shape" data-parallax='{"x": 0, "y": 0, "rotateY": 200}'></div>
      //     <div className="shape" data-parallax='{"x": 0, "y": -40, "rotateY":0}'></div>
      //     <div className="shape" data-parallax='{"x": 0, "y": 0, "rotateZ": 100}'></div>
      //     <div className="shape" data-parallax='{"x": 0, "y": 0, "rotateY": 0}'></div>
      //   </div>
      //   <div className="container">
      //     <div className="row">
      //       <div className="col-12 text-center">
      //         <div className="section-title-center">
      //           <h2 className="mt-n3 mt-md-n4">Try Kalico for free today</h2>
      //         </div>
      //         <form className="form-group mx-auto mt-0">
      //           <input type="email" placeholder="Enter your email to book demo"/>
      //           <button type="submit" className="btn btn-red">Book A Demo</button>
      //         </form>
      //       </div>
      //     </div>
      //   </div>
      // </section>





      // <section className="cta-area section-padding-xl bg-red-150 wow fadeInUp animate__fast">
      //   <div className="container">
      //     <div className="row">
      //       <div className="col-12 text-center">
      //         <div className="section-title-center pb-8">
      //           <h2 className="mt-n3 mt-md-n4">Schedule a Demo</h2>
      //           <p>Let one of our team members show you how Kalico can give your video
      //             content another life
      //           </p>
      //         </div>
      //         <form className="form-group mx-auto mt-0">
      //           <input type="email" placeholder="Enter your email to schedule demo"/>
      //           <button type="submit" className="btn btn-red">Schedule a Demo</button>
      //         </form>
      //       </div>
      //     </div>
      //   </div>
      //   <span className="shape7"></span>
      //   <img className="shape8 d-none d-lg-block" src="/assets/images/shape/shape-8.svg"
      //        alt="Shape"/>
      // </section>
  );
}

export default CallToAction;
