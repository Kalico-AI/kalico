import React from 'react';


function Features() {
  // Check that the DOM has loaded before rendering the page so that
  // we don't get a page without the CSS
  return (
      <section className="features-area-9 pt-125 pb-60">
        <div className="bg-shapes">
          <div className="shape">
            <img src="/assets/images/home_4/banner-shape-2.svg" alt="shape"/>
          </div>
          <div className="shape">
            <img src="/assets/images/home_4/banner-shape-4.svg" alt="shape"/>
          </div>
          <div className="shape">
            <img src="/assets/images/home_4/fea-shape.svg" alt="shape"/>
          </div>
        </div>

        <div className="container" id="features">
          <div className="row">
            <div className="col-lg-8 offset-lg-2">
              <div className="section-title-center">
                <h2 className="wow fadeInUp">Outstanding Features</h2>
                <p className="wow fadeInUp" data-wow-delay="0.2s">
                  Features built to repurpose your video content in minutes rather than days
                </p>
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col-lg-4 col-md-6">
              <div className="features-item text-center wow fadeInLeft">
                <img src="./assets/images/home_4/fea-icon1.svg" alt="Icon"
                     className="features-icon mx-auto mb-30"/>
                <h4 className="features-title">Team Collaboration</h4>
                <p className="features-text">Many desktop publishing packages and web page editors
                  now use Lorem Ipsum as their default model text.</p>
              </div>
            </div>
            <div className="col-lg-4 col-md-6">
              <div className="features-item text-center wow fadeInUp">
                <img src="./assets/images/home_4/fea-icon2.svg" alt="Icon"
                     className="features-icon mx-auto mb-30"/>
                <h4 className="features-title">Time & Attendance</h4>
                <p className="features-text">Many desktop publishing packages and web page editors
                  now use Lorem Ipsum as their default model text.</p>
              </div>
            </div>
            <div className="col-lg-4 col-md-6">
              <div className="features-item text-center wow fadeInRight">
                <img src="./assets/images/home_4/fea-icon3.svg" alt="Icon"
                     className="features-icon mx-auto mb-30"/>
                <h4 className="features-title">Cloud-Based HR</h4>
                <p className="features-text">Many desktop publishing packages and web page editors
                  now use Lorem Ipsum as their default model text.</p>
              </div>
            </div>
            <div className="col-lg-4 col-md-6">
              <div className="features-item text-center wow fadeInLeft">
                <img src="./assets/images/home_4/fea-icon4.svg" alt="Icon"
                     className="features-icon mx-auto mb-30"/>
                <h4 className="features-title">Customizable Payroll</h4>
                <p className="features-text">Many desktop publishing packages and web page editors
                  now use Lorem Ipsum as their default model text.</p>
              </div>
            </div>
            <div className="col-lg-4 col-md-6">
              <div className="features-item text-center wow fadeInUp">
                <img src="./assets/images/home_4/fea-icon5.svg" alt="Icon"
                     className="features-icon mx-auto mb-30"/>
                <h4 className="features-title">Email & Scheduling</h4>
                <p className="features-text">Many desktop publishing packages and web page editors
                  now use Lorem Ipsum as their default model text.</p>
              </div>
            </div>
            <div className="col-lg-4 col-md-6">
              <div className="features-item text-center wow fadeInRight">
                <img src="./assets/images/home_4/fea-icon6.svg" alt="Icon"
                     className="features-icon mx-auto mb-30"/>
                <h4 className="features-title">Career Portal</h4>
                <p className="features-text">Many desktop publishing packages and web page editors
                  now use Lorem Ipsum as their default model text.</p>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default Features;
