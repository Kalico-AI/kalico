import Head from 'next/head';
import {FC} from "react";
import {urls, API_KEY} from '@/utils/paths'
import {DocumentDetail} from "@/types/types";
import DetailView from "@/components/Blog/DetailView";
import {useRouter} from "next/router";

export async function getServerSideProps(context) {
  let document = null
  try {
    const response = await fetch(`${urls.getPostBySlug}/${context.query.slug}?apiKey=${API_KEY}`)
    document = JSON.parse(await response.text())
  } catch (e) {
    console.log(e)
  }
  return {
    props: {
      post: document.post ? document.post : null
    }
  }
}

interface PostProps {
  post?: DocumentDetail
}

const Post: FC<PostProps> = (props) => {
  const router = useRouter()
  if (!props.post) {
    router.push("/blog")
    .catch(e => console.log(e))
  }
  return (
    <section>
      <Head>
        <title>{props.post?.title}</title>
        <meta name="description" content={props.post?.description}/>
      </Head>
      <DetailView post={props.post}/>
    </section>
  )
}

export default Post