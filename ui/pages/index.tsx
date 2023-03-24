import React, {FC} from 'react';
import Head from "next/head";
import RecipeLanding from "@/pages/Recipe";
import {RecipeApi} from "@/api";

const items = [
  {
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg',
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…'
  },
  {    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'Velveeta Hamburger Helper {One Pot!}',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'Homemade Spaghetti Sauce with Ground Beef',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
    description: 'This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…',
    date: 'March 1, 2023',
    title: 'How to Cook Frozen Pork Chops in the Oven',
    url: 'https://getonmyplate.com/how-to-cook-frozen-pork-chops-in-the-oven/',
    imgUrl: 'https://getonmyplate.com/wp-content/uploads/2023/02/frozen-pork-chops-in-the-oven-1-8-600x850.jpg'
  },
  {
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
  const response = await new RecipeApi().getAllRecipes(page, limit)
  console.log("Response: ", response.data)
  return {
    props: {
      posts: items
    }
  }
}

export interface IndexProps {
  posts: []
}
const Index: FC<IndexProps> =  (props) => {
  return (
    <>
      <Head>
        <title>Kalico | Best Recipes</title>
      </Head>
      <RecipeLanding posts={props.posts}/>
    </>
  );
}
export default Index
