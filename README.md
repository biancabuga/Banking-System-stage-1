# Proiect_1_poo
### **Proiect etapa 1 - Buga Bianca Gabriela 321CD**
Acest proiect simuleaza un sistem bancar conceput pentru 
gestionare utilizatorilor, conturilor si tranzactiilor financiare.Proiectul
permite extensibilitatea, acesta incluzand 3 design pattern-uri : 
Builder, Factory si Singleton.

---

## **Structura proiectului**
- User – Gestionează utilizatorii și conturile acestora.
- Account – Reprezintă conturile bancare și operațiunile aferente.
- Card – Gestionează informațiile și statusul cardurilor.
- Transaction – Modelează tranzacțiile financiare.
- Converter – Asigură conversia valutară.
- Main – Punctul de intrare în aplicație, gestionează logica principală.

# **Functionalitati**
1. **Gestionarea utilizatorilor** :
- crearea utilizatorilor
- posibilitatea ca un utilizator sa aiba mai multe conturi

2. **Gestionarea conturilor** :
- crearea de conturi bancare
- gestionarea soldului
- gestionarea tipului de cont (savings sau classic)
- setarea unui sold minim
- calcularea dobanzilor pentru conturile de economii

3. **Gestionarea cardurilor** :
- crearea cardurilor (normal sau one_time)
- activarea, blocarea sau deblocarea cardurilor pe baza soldului
si a unei limite impuse
- adaugarea cardurilor la conturi bancare existente

4. **Tranzactii financiare** :
- datele din tranzactii sunt afisate conform unui tip care i se
atribuie tranzactiei la crearea in comanda
- transferuri intre conturi bancare - se creeaza tranzactie atat pentru contul
care depune banii cat si pentru cel care primeste banii si trebuie
sa facem conversia valutara daca contul in care vrem sa depunem bani
nu are acelasi tip de moneda
- plati online cu cardul - daca avem card de tip one_time, dupa
ce facem plata trebuie sa il stergem si sa generam altul
- tranzactii impartite la mai multe conturi - trebuie sa ne asiguram
ca verificam daca toate conturile au suficienti bani, si in cazul
in care nu au, nu facem plata si pastram ultimul cont care nu are
suficienti bani
- generarea de rapoarte despre tranzactii - trebuie sa afisam comenzile 
unui cont avand date din input un interval de interes

5. **Conversia valutara** :
- asigura conversia intre monede folosind cursuri valutare
predefinite
- conversia se realizeaza prin intermediul unui graf care
foloseste algoritmul de cautare in adancime pentru a gasi drumul
intre 2 noduri oarecare

6. **Rapoarte** :
- generarea de rapoarte pentru tranzactii si istoricul
comenzilor unui cont
