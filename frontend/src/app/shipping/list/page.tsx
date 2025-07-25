"use client";

import { useEffect, useState } from "react";
import Link from "next/link";

interface ShippingItem {
  orderNo: number;
  status: string;
  orderDate: string;
  recipient: string;
  address: string;
  postcode: number;
  phoneNumber: string;
  carrier: string;
}

export default function ShippingListPage() {
  const [allShippings, setAllShippings] = useState<ShippingItem[]>([]);
  const [currentTime, setCurrentTime] = useState<string>('');
  const [currentPage, setCurrentPage] = useState(1);
  const [currentLatePage, setCurrentLatePage] = useState(1);
  const [searchEmail, setSearchEmail] = useState('');
  const [currentCompletedPage, setCurrentCompletedPage] = useState(1);
  const lateItemsPerPage = 3;  // 페이지 목록 원하는 수
  const itemsPerPage = 3; // 페이지 목록 원하는 수
  const completedItemsPerPage = 3;  // 배송 완료 목록 페이지당 수


  useEffect(() => {
    const fetchShippings = () => {
      const url = searchEmail
        ? `http://localhost:8080/api/shippings?email=${searchEmail}`
        : "http://localhost:8080/api/shippings";

      fetch(url)
        .then((res) => res.json())
        .then((data) => {
          const mappedData: ShippingItem[] = data.map((item: any) => ({
            orderNo: item.id,
            status: item.status,
            orderDate: item.createDate
              ? new Date(item.createDate).toISOString()
              : '',
            recipient: item.email,
            address: item.address,
            postcode: item.postcode,
            phoneNumber: item.contactNumber,
            carrier: item.carrier,
          }));
          setAllShippings(mappedData);
        })
        .catch((err) => console.error("배송 데이터 로딩 실패", err));
    };

    fetchShippings();
    const intervalId = setInterval(fetchShippings, 10000);
    return () => clearInterval(intervalId);
  }, [searchEmail]);


  useEffect(() => {
    const updateTime = () => {
      const now = new Date();
      const formattedTime = now.toLocaleString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false,
      });
      setCurrentTime(formattedTime);
    };

    updateTime();
    const intervalId = setInterval(updateTime, 1000);
    return () => clearInterval(intervalId);
  }, []);

    const now = new Date();
    const today14 = new Date(
      now.getFullYear(),
      now.getMonth(),
      now.getDate(),
      14, 0, 0
    );

  const filteredShippings = allShippings.filter(item =>
    searchEmail === '' || item.recipient.toLowerCase().includes(searchEmail.toLowerCase())
  );

  // 오전 14시 이전 주문
  const morningAll = filteredShippings.filter(item => {
      const orderTime = new Date(item.orderDate);
      const hour = orderTime.getHours();

      return ((hour >= 9 && hour < 14) || item.status === 'DELIVERING')
              && item.status !== 'DELIVERED';
  });




  const totalPages = Math.ceil(morningAll.length / itemsPerPage);

  const currentMorningItems = morningAll.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  // 오후 14시 이후 주문
  const lateOrders = filteredShippings.filter(item => {
      const orderTime = new Date(item.orderDate);
      const hour = orderTime.getHours();

      return (hour < 9 || hour >= 14)
              && item.status === 'BEFORE_DELIVERY';
  });




  const totalLatePages = Math.ceil(lateOrders.length / lateItemsPerPage);

  const currentLateItems = lateOrders.slice(
    (currentLatePage - 1) * lateItemsPerPage,
    currentLatePage * lateItemsPerPage
  );

  // 배송 완료 데이터 필터링
  const completedOrders = filteredShippings.filter(item => item.status === 'DELIVERED');

  const totalCompletedPages = Math.ceil(completedOrders.length / completedItemsPerPage);

  const currentCompletedItems = completedOrders.slice(
    (currentCompletedPage - 1) * completedItemsPerPage,
    currentCompletedPage * completedItemsPerPage
  );


  return (
    <div className="p-6 bg-gray-100 min-h-screen mt-20 flex flex-col items-center">
       <div className="flex justify-between items-center w-full max-w-5xl mb-4">
          <h1 className="text-2xl font-bold ml-64">🚚 배송 목록</h1>

          <input
            type="text"
            placeholder="이메일로 검색"
            value={searchEmail}
            onChange={(e) => setSearchEmail(e.target.value)}
            className="border px-3 py-2 rounded w-64 shadow mr-72"
          />
        </div>


      <div className="flex justify-center items-start space-x-8">
        <div className="overflow-x-auto bg-white shadow rounded-lg w-[800px] p-4">

        {/* 배송중 */}
        {morningAll.length > 0 ? (
          <div className="mt-8">
            <h2 className="text-xl font-bold mb-4 text-left">📦 배송중</h2>
            <table className="min-w-full border border-gray-300 text-center text-sm">
              <thead className="bg-gray-200">
                <tr>
                  <th className="border p-2">주소</th>
                  <th className="border p-2">우편번호</th>
                  <th className="border p-2">전화번호</th>
                  <th className="border p-2">이메일</th>
                  <th className="border p-2">배송업체</th>
                  <th className="border p-2">상태</th>
                  <th className="border p-2">주문일자</th>
                  <th className="border p-2">Id</th>
                </tr>
              </thead>
              <tbody>
                {currentMorningItems.map((item, index) => (
                  <tr key={index} className="hover:bg-gray-50">
                    <td className="border p-2">{item.address}</td>
                    <td className="border p-2">{item.postcode}</td>
                    <td className="border p-2">{item.phoneNumber}</td>
                    <td className="border p-2">{item.recipient}</td>
                    <td className="border p-2">{item.carrier}</td>
                    <td className="border p-2">{item.status}</td>
                    <td className="border p-2">
                      {new Date(item.orderDate).toLocaleString('ko-KR', {
                        year: '2-digit',
                        month: '2-digit',
                        day: '2-digit',
                        hour: '2-digit',
                        minute: '2-digit',
                        second: '2-digit',
                        hour12: false,
                      })}
                    </td>
                    <td className="border p-2">{item.orderNo}</td>
                  </tr>
                ))}
              </tbody>
            </table>

            <div className="flex justify-center items-center mt-4 space-x-2">
              <button onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                disabled={currentPage === 1}
                className="bg-gray-300 px-3 py-1 rounded disabled:opacity-50">◀ 이전
              </button>
              <span>{currentPage} / {totalPages}</span>
              <button onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
                disabled={currentPage === totalPages}
                className="bg-gray-300 px-3 py-1 rounded disabled:opacity-50">다음 ▶
              </button>
            </div>
          </div>
        ) : (
          <div className="mt-8 text-center text-gray-500">
            📦 배송중인 주문이 없습니다.
          </div>
        )}

          {/* 오늘 14시 이후 주문 목록 */}
          {lateOrders.length > 0 ? (
            <div className="mt-8">
              <h2 className="text-xl font-bold mb-4 text-left">📭 배송 대기</h2>
              <table className="min-w-full border border-gray-300 text-center text-sm">
                <thead className="bg-gray-200">
                  <tr>
                    <th className="border p-2">주소</th>
                    <th className="border p-2">우편번호</th>
                    <th className="border p-2">전화번호</th>
                    <th className="border p-2">이메일</th>
                    <th className="border p-2">배송업체</th>
                    <th className="border p-2">상태</th>
                    <th className="border p-2">주문일자</th>
                    <th className="border p-2">Id</th>
                  </tr>
                </thead>
                <tbody>
                  {currentLateItems.map((item, index) => (
                    <tr key={index} className="hover:bg-gray-50">
                      <td className="border p-2">{item.address}</td>
                      <td className="border p-2">{item.postcode}</td>
                      <td className="border p-2">{item.phoneNumber}</td>
                      <td className="border p-2">{item.recipient}</td>
                      <td className="border p-2">{item.carrier}</td>
                      <td className="border p-2">{item.status}</td>
                      <td className="border p-2">
                        {new Date(item.orderDate).toLocaleString('ko-KR', {
                          year: '2-digit',
                          month: '2-digit',
                          day: '2-digit',
                          hour: '2-digit',
                          minute: '2-digit',
                          second: '2-digit',
                          hour12: false
                        })}
                      </td>
                      <td className="border p-2">{item.orderNo}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <div className="flex justify-center items-center mt-4 space-x-2">
                    <button
                      onClick={() => setCurrentLatePage((prev) => Math.max(prev - 1, 1))}
                      disabled={currentLatePage === 1}
                      className="bg-gray-300 px-3 py-1 rounded disabled:opacity-50"
                    >
                      ◀ 이전
                    </button>
                    <span>{currentLatePage} / {totalLatePages}</span>
                    <button
                      onClick={() => setCurrentLatePage((prev) => Math.min(prev + 1, totalLatePages))}
                      disabled={currentLatePage === totalLatePages}
                      className="bg-gray-300 px-3 py-1 rounded disabled:opacity-50"
                    >
                      다음 ▶
                    </button>
                  </div>
                </div>
              ) : (
                <div className="mt-8 text-center text-gray-500">
                  📭 배송 대기 중인 주문이 없습니다.
                </div>
              )}

        {/* 배송 완료 */}
        {completedOrders.length > 0 ? (
          <div className="mt-8">
            <h2 className="text-xl font-bold mb-4 text-left">✅ 배송 완료</h2>
            <table className="min-w-full border border-gray-300 text-center text-sm">
              <thead className="bg-gray-200">
                <tr>
                  <th className="border p-2">주소</th>
                  <th className="border p-2">우편번호</th>
                  <th className="border p-2">전화번호</th>
                  <th className="border p-2">이메일</th>
                  <th className="border p-2">배송업체</th>
                  <th className="border p-2">상태</th>
                  <th className="border p-2">주문일자</th>
                  <th className="border p-2">Id</th>
                </tr>
              </thead>
              <tbody>
                {currentCompletedItems.map((item, index) => (
                  <tr key={index} className="hover:bg-gray-50">
                    <td className="border p-2">{item.address}</td>
                    <td className="border p-2">{item.postcode}</td>
                    <td className="border p-2">{item.phoneNumber}</td>
                    <td className="border p-2">{item.recipient}</td>
                    <td className="border p-2">{item.carrier}</td>
                    <td className="border p-2">{item.status}</td>
                    <td className="border p-2">
                      {new Date(item.orderDate).toLocaleString('ko-KR', {
                        year: '2-digit',
                        month: '2-digit',
                        day: '2-digit',
                        hour: '2-digit',
                        minute: '2-digit',
                        second: '2-digit',
                        hour12: false
                      })}
                    </td>
                    <td className="border p-2">{item.orderNo}</td>
                  </tr>
                ))}
              </tbody>
            </table>

            {/* ✅ 페이지네이션 버튼 */}
            <div className="flex justify-center items-center mt-4 space-x-2">
              <button
                onClick={() => setCurrentCompletedPage((prev) => Math.max(prev - 1, 1))}
                disabled={currentCompletedPage === 1}
                className="bg-gray-300 px-3 py-1 rounded disabled:opacity-50"
              >
                ◀ 이전
              </button>
              <span>{currentCompletedPage} / {totalCompletedPages}</span>
              <button
                onClick={() => setCurrentCompletedPage((prev) => Math.min(prev + 1, totalCompletedPages))}
                disabled={currentCompletedPage === totalCompletedPages}
                className="bg-gray-300 px-3 py-1 rounded disabled:opacity-50"
              >
                다음 ▶
              </button>
            </div>

          </div>
        ) : (
          <div className="mt-8 text-center text-gray-500">
            ✅ 배송 완료된 주문이 없습니다.
          </div>
        )}

        </div>

        <div className="p-4 bg-white rounded shadow text-center w-80">
            <p className="font-semibold mb-2">
                📢 당일 오후 2시 이후의 주문은 <br />
                다음날(오전 9시) 배송을 시작합니다!
            </p>

            <p className="text-sm mb-5">
                📌 주문일자 → <strong>00:00 ~ 08:59</strong> <br />
                초기 Status = <strong>BEFORE_DELIVERY</strong> <br />
                오전 9시 스케줄러 → <strong>DELIVERING</strong> 으로 변경
            </p>

            <p className="text-sm mb-5">
                📌 주문일자 → <strong>09:00 ~ 13:59</strong> (배송시간) <br />
                초기 Status = <strong>DELIVERING</strong> <br />
                스케줄러가 건드리지 않음 (이미 DELIVERING이므로)
            </p>

            <p className="text-sm mb-5">
                📌 주문일자 → <strong>14:00 ~ 다음날 08:59</strong> <br />
                초기 Status = <strong>BEFORE_DELIVERY</strong> <br />
                오전 9시 스케줄러 → <strong>DELIVERING</strong> 으로 변경
            </p>

            <p className="text-sm mb-5">
                📌
                modifyDate가 있으면 modifyDate 기준 <strong>10초 후</strong>에 <strong>DELIVERED</strong> 변경 <br />
                없으면 createDate 기준 <strong>10초 후</strong>에 <strong>DELIVERED</strong> 변경 <br />
                (실제 운영에서는 2시간 후 변경 예정)
            </p>

            <p className="text-sm mb-5">
                🔄 Status가 변경되면 <br />
                해당 주문은 변경된 Status에 맞는 목록으로 자동 이동합니다!<br />
                (자동 새로고침: 10초마다)
            </p>

            <p className="text-sm mb-5">
                ⚡ 현재는 테스트를 위해 스케줄러가 <br /> <strong>10초</strong>마다 실행됩니다. <br />
                (실제는 오전 9시에 실행)
            </p>

            <p>⏰ 현재 시간: <span className="font-mono">{currentTime}</span></p>
        </div>



      </div>
    </div>
  );
}
