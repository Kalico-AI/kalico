import React from 'react';


function FAQ() {
  return (
      <section className="faq-area wow fadeInUp animate__fast">
        <div className="container" id="support">
          <div className="row">
            <div className="col-12">
              <div className="section-title-center">
                <h2 className="font-bold mt-n3 mt-md-n4">Frequently asked questions</h2>
                <p>You can find answers to the most common questions people ask us here.</p>
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
                       How does the AI work?
                    </button>
                  </h2>
                  <div id="project-collapseOne" className="accordion-collapse collapse"
                       aria-labelledby="project-headingOne" data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                        Our AI works by extracting audio contained in the video to write the article. We use a combination of state-of-the-art open source AI tools and our own models.
                      </p>
                    </div>
                  </div>
                </div>
                <div className="accordion-item">
                  <h2 className="accordion-header" id="project-headingOneHalf">
                    <button
                        className="accordion-button collapsed"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#project-collapseOneHalf"
                        aria-expanded="false"
                        aria-controls="project-collapseOneHalf"
                    >
                      Is the article plagiarism free?
                    </button>
                  </h2>
                  <div id="project-collapseOneHalf" className="accordion-collapse collapse"
                       aria-labelledby="project-headingOneHalf" data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                        Our AI ensures 100% plagiarism-free output by using your original content to maintain contextual integrity while significantly improving it, instead of inventing content like other well-known content writing platforms.
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
                      What types of links do you support?
                    </button>
                  </h2>
                  <div id="project-collapseTwo" className="accordion-collapse collapse"
                       aria-labelledby="project-headingTwo" data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                       We support YouTube and Instagram links right now. Support for other platforms coming soon.
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
                      Which languages are supported?
                    </button>
                  </h2>
                  <div id="project-collapseThree" className="accordion-collapse collapse"
                       aria-labelledby="project-headingThree"
                       data-bs-parent="#projectAccordion">
                    <div className="accordion-body">
                      <p>
                        Kalico supports English. We will be adding support for more languages soon.
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
  );
}

export default FAQ;
