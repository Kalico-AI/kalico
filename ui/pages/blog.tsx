import Head from 'next/head';
import {urls} from '@/utils/paths'
import {DocumentDetail} from "@/types/types";
import {FC} from "react";
import ListView from "@/components/Blog/ListView";
import BlogHero from "@/components/Blog/hero";

export async function getServerSideProps() {
  const response = await fetch(urls.getAllPosts)
  const posts = await response.text()
  let textResponse = []
  try {
    textResponse = posts ? JSON.parse(posts)?.posts : []
  } catch (e) {
    console.log(e)
  }
  console.log(textResponse)
  return {
    props: {
      posts: textResponse
    }
  }
}

interface IndexProps {
  posts?: DocumentDetail[]
}

const Index: FC<IndexProps> = (props) => {
  return (
      <section>
        <Head>
            <title>Kalico Blog</title>
            <meta name="description" content="Kalico is an AI platform for repurposing your audio and video content"/>
        </Head>
        <BlogHero/>
        <ListView posts={props.posts}/>
    </section>
  )
}

export default Index