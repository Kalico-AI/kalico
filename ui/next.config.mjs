/**
 * @type {import('next').NextConfig}
 */
 const nextConfig = {
  basePath: "/ui",
  async redirects() {
    return [
      {
          source: '/',
          destination: '/ui',
          basePath: false,
          permanent: false
      }
    ]
  },
  images: {
    unoptimized: true,
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'static.justboil.me',
      },
    ],
  },
}

export default nextConfig