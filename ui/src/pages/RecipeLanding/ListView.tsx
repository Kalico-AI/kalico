import React from 'react';
import {Grid} from "@mui/material";


function Hero() {
  const items = [
    {
      date: 'March 1, 2023',
      title: 'How to Cook Frozen Pork Chops in the Oven',
      url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
      imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
    },
    {
      date: 'March 1, 2023',
      title: 'Velveeta Hamburger Helper {One Pot!}',
      url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
      imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
    },
    {
      date: 'March 1, 2023',
      title: 'Homemade Spaghetti Sauce with Ground Beef',
      url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
      imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
    },
    {
      date: 'March 1, 2023',
      title: 'How to Cook Frozen Pork Chops in the Oven',
      url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
      imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
    },
    {
      date: 'March 1, 2023',
      title: 'How to Cook Frozen Pork Chops in the Oven',
      url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
      imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
    },
    {
      date: 'March 1, 2023',
      title: 'How to Cook Frozen Pork Chops in the Oven',
      url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
      imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
    },
    {
      date: 'March 1, 2023',
      title: 'How to Cook Frozen Pork Chops in the Oven',
      url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
      imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
    }
  ]
  return (
      <main className="list-view-container">
        <Grid container className="list-items" spacing={3}>
          {
            items.map((it, index) => {
              return (
                  <Grid item sm={3} key={index}>
                    <article
                        className="list-item-card"
                        aria-label={it.title}>
                      <a href={it.url} className="alignnone" aria-hidden="true">
                        <img width="600" height="850"
                             src={it.imgUrl}
                             alt={it.title} decoding="async"
                             data-lazy-src={it.imgUrl}
                             data-ll-status="loaded"/>
                        <noscript><img width="600" height="850"
                                       src={it.imgUrl}
                                       alt={it.title} decoding="async"/>
                        </noscript>
                      </a>
                      <header className="entry-header">
                        <p className="entry-meta">
                          <time className="entry-time">{it.date}</time>
                        </p>
                        <h2 className="entry-title"><a
                            href={it.url}>{it.title}</a></h2>
                      </header>
                    </article>
                  </Grid>
              )
            })
          }

        </Grid>

      </main>
  );
}

export default Hero;
