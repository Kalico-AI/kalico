import React, {FC} from 'react';
import {Box, Grid} from "@mui/material";

export interface GridViewProps {
  posts: []
}
const GridView: FC<GridViewProps> =  (props) => {
  return (
      <main className="recipe-container">
        <Grid container className="grid-items" spacing={4}>
          {
            props.posts?.map((it, index) => {
              const url = '/recipe/' + it.slug
              return (
                  <Grid item sm={4} key={index}>
                    <article
                        className="grid-item-card"
                        aria-label={it.title}>
                      <a href={url} className="alignnone" aria-hidden="true">
                        <img width="600" height="850"
                             src={it.imgUrl}
                             alt={it.title}/>
                      </a>
                      <Box className="entry-header">
                        <span className="entry-meta">
                          <time className="entry-time">{it.date}</time>
                        </span>
                        <h2 className="entry-title"><a
                            href={url}>{it.title}</a></h2>
                      </Box>
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
