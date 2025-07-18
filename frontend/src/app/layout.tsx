import type { Metadata } from "next";
import Image from "next/image";
import { Geist, Geist_Mono } from "next/font/google";
import Link from "next/link";
import "./globals.css";
import Header from "@/components/Header";
import { AuthProvider } from "@/contexts/AuthContext";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Caffe Menu Service",
  description: "NBE6-8-1-Team 12",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body>
        <AuthProvider>
          {/* Header 컴포넌트 호출 */}
          <Header />

          {/* 1. 흐릿한 배경을 위한 div 추가 */}
          <div className="background-blur"></div>

          {/* 2. 콘텐츠를 담을 div (배경 위에 위치) */}
          <div className="content-wrapper">
            {/* 메인 콘텐츠 */}
            <main className="flex-1 flex">{children}</main>

            {/* 하단 푸터 */}
            <footer className="footer-container">
              <p>@copyright NBE6-8-Team12</p>
            </footer>
          </div>
        </AuthProvider>
      </body>
    </html>
  );
}
