# Курсовая работа по учебной дисциплине "Теория автоматов и формальных языков"

## Pascal code :
```
var a,b,c : integer;
begin 
a:=3; b:=a;
if a>b then c:=a
else begin
c:=b; b:=1;
end;
end.
```
## Грамматика :
```
program -> var listDesription begin listOperation end .
listDesription -> description ; altDescription
altDescription -> listDesription | ε
description -> listVariable : type
listVariable -> IDENTIFIER altListVariable
altListVariable -> , listVariable | ε
type -> integer | real | double
listOperation -> operation ; listOperation | ε
operation -> appropriation | comparison
appropriation -> IDENTIFIER := operand
comparison -> if logicalExpression then blockOperation altBlockOperation
altBlockOperation -> else blockOperation | ε
blockOperation -> appropriation | begin listOperation end
operand -> IDENTIFIER | NUMBER
logicalExpression -> operand compare operand
compare -> < | >
```
## Множества FIRST & FOLLOW:

### FIRST
```
Нетерминал	      FIRST
program	              var
listDesription	      IDENTIFIER, ε
description	      IDENTIFIER
altDescription	      IDENTIFIER, ε
listVariable	      IDENTIFIER
altListVariable	      ,, ε
type	              integer, real, double
listOperation	      IDENTIFIER, if, ε
operation	      IDENTIFIER, if
appropriation	      IDENTIFIER
comparison	      if
altBlockOperationelse , ε
blockOperation	      IDENTIFIER, begin
operand	              IDENTIFIER, NUMBER
logicalExpression     IDENTIFIER, NUMBER
compare	              <, >
```
### FOLLOW
```
Нетерминал	      FOLLOW
program	              $
listDesription	      begin
description	      ;
altDescription	      begin
listVariable	      :
altListVariable	      :
type	              ;
listOperation	      end, else
operation	      ;, end, else
appropriation	      ;, end, else
comparison	      ;, end, else
altBlockOperationend  , else
blockOperation	      ;, end, else
operand	              <, >, then
logicalExpression     then
compare	              IDENTIFIER, NUMBER
```
