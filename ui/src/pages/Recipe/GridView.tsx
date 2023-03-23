import React, {FC} from 'react';
import {Grid} from "@mui/material";

export interface GridViewProps {
  posts: []
}
const GridView: FC<GridViewProps> =  (props) => {
  return (
      <main className="recipe-container">
        <Grid container className="grid-items" spacing={3}>
          {
            props.posts?.map((it, index) => {
              return (
                  <Grid item sm={3} key={index}>
                    <article
                        className="grid-item-card"
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

export default GridView;
