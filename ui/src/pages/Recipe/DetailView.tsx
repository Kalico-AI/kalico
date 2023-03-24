import React, {FC} from 'react';
import {Box, Divider, Grid} from "@mui/material";
import {TopRecipesProps} from "@/pages/Recipe/TopRecipes";
import Link from "next/link";
import {urls} from "@/utils/paths";
import RestaurantMenuIcon from '@mui/icons-material/RestaurantMenu';
import SummaryComponent from "@/pages/Recipe/SummaryComponent";


export interface DetailViewProps {
  post: object
}

const DetailView: FC<DetailViewProps> =  (props) => {
  return (
      <article className="recipe-container recipe-detail">
        <Grid container className="recipe-detail-top">
          <Grid item md={6} sm={12}>
            <Box className="primary-thumbnail">
              <img src={props.post.imgUrl} alt={props.post.title}/>
            </Box>
          </Grid>
          <Grid item md={6} sm={12} sx={{p: 3}}>
            <Box className="title">
              <h3 >{props.post.title}</h3>
            </Box>
            <SummaryComponent steps={12} ingredients={22} time={40}/>
          </Grid>
        </Grid>
        <Divider><RestaurantMenuIcon/></Divider>
        <Box className="recipe-detail-bottom">
          <Box className="summary">
            <p>{props.post.summary}</p>
          </Box>
          <Box className="ingredients">
            <h3>Ingredients</h3>
            <ul>
              {
                props.post?.ingredients?.map((it, index) => {
                  return <li key={"ingredient-" + index}><span>{it}</span></li>
                })
              }
            </ul>
          </Box>
          <Box className="instructions">
            <h3>Instructions</h3>
            <ul>
              {
                props.post?.instructions?.map((it, index) => {
                  return <li key={"step-" + index}><span>{it}</span></li>
                })
              }
            </ul>
          </Box>
        </Box>

      </article>
  );
}

export default DetailView;
