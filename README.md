# delivery
Transaction managementREST

Mahsulot yetkazib berishga ximat qiluvchi mobil dasturi uchun REST API yaratish. 
	
Tizim quyidagilardan tashkil topgan:

1.Hudud va shu hududda joylashgan joylar (regions and places located in the region)

2.Tashuvchi yani curyer (Carrier)

3.Biror bir mahsulotga bo’lgan so’rov (Request)

4.Mahsulotlarga taklif (Offer)

5.Va transaksiyalar (Transactions)

Tizim quyidagi vazifalarni bajarishi kerak (GET, POST, DELETE, PUT): 

1.Foydalanuvchi uchun api request lar

a.Ro’yxatdan o’tish

b.Tizimga kirish

2.Transaktsiyalar uchun api request lar


a.(addRegion) Hudud nomini va shu hududdagi joy nomlarini tizimga qo’shish. Tizimda mavjud bo'lgan joylar qo'shilmaydi. Api method qo'shilgan joylar	nomlarining tartiblangan ro'yxatini qaytaradi

b.(addCarrier) Carrier ni tizimga qo’shish, bunda carrier ning ismi va u xizmat ko’rsatadigan hududlar nomlari (regionNames) tizimga jo’natiladi. Tizimda	mavjud bo’lmagan va duplicate hudud nomlari etiborga olinmaydi. Api method carrier xizmat qiladigan hududlarning tartiblangan ro’yxatini qaytaradi.

c.(getCarriersForRegion )Tizimga hudud nomi (regionName) jo’natilganda shu hududga xizmat qiladigan carrier lar ning tartiblangan (carrierning ismi bo’yicha) ro’yxatini qaytaradi.

d.(addRequest ) Tizimga so’rov kodi (String requestId) yetkazib berish joyi (placeName) va soʻralgan mahsulot identifikatori (String productId) jo’natilganda tizimga yangi so’rov (request) qo’shadi.  Agar joy aniqlanmagan bo'lsa yoki kod takrorlangan bo'lsa, xatolik sababini ko’rsatuvchi standard 
api error message qaytaradi.

e.(addOffer ) Tizimga taklif kodi (String offerId), qabul qilish joyi (placeName) va taklif qilingan mahsulot identifikatori (String productId) jo’natilganda tizimga yangi taklif (offer) qo’shadi.  Agar joy aniqlanmagan bo'lsa yoki kod takrorlangan bo'lsa, xatolik sababini ko’rsatuvchi standard api error message qaytaradi.

f.(addTransaction) Tizimga tranzaksiyaning kodi (transactionId), tashuvchi nomi (carrierName), soʻrov va taklif identifikatorlari (requestId, offerId) jo’natilganda tizimga yangi transaksiya (transaction) qo’shadi.  Agar carrier, request yoki offer mavjud bo’lmasa standard api error message qaytaradi. Agar quyidagi talablarning barchasi qanoatlantirilsa tranzaktsiya so'rov va taklif bilan bog'lanishi kerak, aks holda xatolik sababini ko’rsatuvchi api error message qayariladi, talablar: 

i.so'rov(request) va taklif (offer) oldingi tranzaktsiyalar bilan bog'lanmagan bo'lishi kerak

ii.so'rov(request) va taklif (offer) bir xil mahsulot identifikatoriga tegishli bo'lishi kerak.

iii. Tashuvchi (carrier) ham etkazib berish, ham olib ketish joylariga (ya'ni, tegishli hududlarga) xizmat qilishi kerak

g.(evaluateTransaction) Tizimga tranzaksiyaning kodi (transactionId) va ball (int score) jo’natish orqali transaksiyaga ball beriladi. Yangi transaksiya Yaratilganda tranzaksiya 0 ga teng ballga ega bo'ladi. Agar ball 1 va 10 orasida bo'lmasa (ekstremallar kiradi) false qaytariladi, aks holda true qaytariladi.

3.Statisticalar uchun api request lar
(deliveryRegionsPerNT) api methodida bir xil miqdordagi tranzaktsiyalar uchun etkazib berish joylari bo'lgan hududlar nomlari ro'yxatini (alifbo tartibida tartiblangan) qaytaradi. Ya’ni har bir huduga tegishli transaksiya sonlari (transactionNumber bo’lsin) topiladi va shu transaction number va qaysi hududlarni tranasaksiyalar soni shu transaction numberga tengligi qaytariladi.

a.Hudud tranzaktsiyaga tegishli so'rovni yetkazib berish joyini o'z ichiga olgan bo'lsa, tranzaktsiyani yetkazib berish joyidir. Qaytarilgan response da tranzaktsiyalar soni kamayish tartibida bo’lishi kerak.

b.(scorePerCarrier) Tizimga minimumScore (int minimumScore) qiymat jo’natish orqali tashuvchilar (carriers) va bir xil tashuvchiga (carrier ga) tegishli tranzaksiyalarning umumiy bali qaytariladi. bali berilgan minimal balldan (minimumScore) past boʻlgan tranzaksiya eʼtiborga olinmaydi. Tashuvchilar (carriers) alifbo tartibida saralangan holda qaytariladi.
(nTPerProduct) har bir masulot uchun (productId) uchun tranzaktsiyalar sonini (faqat 0 dan katta bo'lsa) qaytaradi, masulot identificatori bo’yicha saralangan holatda qaytishi kerak.

Talablar:

Java, Spring

Postgres

Api da ma’lumot almashinish json formatda

Har hir api request uchun mos http methodlar (GET, POST, PUT, DELETE) va mos endpointlarni tanlash

Vazifani baholashda quyidagilarga etibor beriladi:

Vazifadagi talablarni to’gri tushunish qobiliyati

Vazifadagi talablarni optimal darajada bajarish
