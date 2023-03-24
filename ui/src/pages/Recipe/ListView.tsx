import React, {FC} from 'react';
import {Box, Grid} from "@mui/material";
import Link from "next/link";
import SummaryComponent from "@/pages/Recipe/SummaryComponent";


export interface ListViewProps {
  posts: []
}

const ListView: FC<ListViewProps> =  (props) => {
  return (
      <main className="recipe-container">
        <Grid container className="list-items" spacing={2}>
          {
            props.posts?.map((it, index) => {
              const url = '/recipe/' + it.slug
              return (
                  <>
                    <Grid item md={6} sm={12} key={index}>
                      <Box
                          className="list-item-card center">
                        <a href={url} aria-hidden="true">
                          <img src={it.imgUrl} alt={it.title}/>
                        </a>
                      </Box>
                    </Grid>
                    <Grid item md={6} sm={12} key={index}>
                      <Box
                          className="list-item-card middle">
                        <Box className="entry-header">
                          <p className="entry-meta">
                            <time className="entry-time">{it.date}</time>
                          </p>
                          <h2 className="entry-title"><a
                              href={url}>{it.title}</a></h2>
                          <p>{it.description}</p>
                        </Box>
                        <SummaryComponent steps={12} ingredients={22} time={40}/>
                        <Box className="list-view-read-more">
                          <Link href={url}>
                            <button
                                className="read-more-button"
                            >View Recipe</button>
                          </Link>
                        </Box>
                      </Box>

                    </Grid>
                  </>

              )
            })
          }

        </Grid>

      </main>
  );
}

export default ListView;
