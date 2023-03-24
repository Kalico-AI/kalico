import React, {FC, useEffect, useState} from 'react';
import {PATHS} from "@/utils/constants";
import {ProjectApi, ProjectDetail} from "@/api";
import {headerConfig} from "@/api/headerConfig";
import MyProject from "@/pages/Dashboard/MyProject";
import {toast} from "react-toastify";
import {useRouter} from "next/router";
import Head from "next/head";
import {auth} from "@/utils/firebase-setup";
import ListView from "@/pages/Recipe/ListView";



const items = [
  {
    slug: 'how-to-cook-chicken-well',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'Velveeta Hamburger Helper {One Pot!}',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'Homemade Spaghetti Sauce with Ground Beef',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    slug: 'how-to-cook-chicken-well',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  }
]

export async function getServerSideProps(context) {
  const page = context.query.page - 1
  const limit = 10
  return {
    props: {
      posts: items
    }
  }
}

interface RecipeListProps {
  posts: []
}


const RecipeList: FC<RecipeListProps> =  (props) => {
  return (
      <>
        <Head>
          <title>Kalico</title>
        </Head>
        <main>
          {/*<section className="container">*/}
            <ListView posts={props.posts}/>
          {/*</section>*/}
        </main>

      </>
  );
}

export default RecipeList
