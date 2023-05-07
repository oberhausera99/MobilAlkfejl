regisztráció - MainActivity és SignupActivity
adatmodell - Product.java
activityk - Main,Signup,ProductList,ProductListAllActivity
layoutok - activity_main és activity_signup ConstraintLayout, a többiben Relative és Linear

A main és a signup activity nem reszponzív, a többi igen

animációk:
a bejelentkezés gomb kattintáskor elmozdul (textslide)
a ProductList activity balról jobbra csúcszik be (slide_in_right, slide_out_left)

android permissionos erőforráshasználat nincs

notification - vásárlás esetén értesítést küld az alkalmazás

lifecycle hook: az OnDestroyban jelentkeztetem ki a felhasználót

CRUD a ProductList és ProductListAll activitykbn, nem futnak külön szálon

lekérdezés, rendezés: a 'saját hirdetésekben' csak a bejelentkezett felhasználó hirdetései vannak megjelenítve, az 'összes hirdetésben' mindenki másé

