# Praktikum-2023

## Modules used
- Flatlaf
- Jgraphx
- Json

## Description
Can be used to visualize the hirarchical structure of paths in the software crowd:it by accu:rate (📍 Germany)

## Usage
To use this software, first clone the github repo and open <NAME>.json. The json follows the following structure:
```json
{
   "paths":[
      {
         "name":"String"
      }
   ],
   "subpaths":[
      {
         "name":"String",
         "names":[
            "String"
         ],
         "capacity":[
            "int"
         ],
         "probs":[
            "float"
         ],
         "type":"int"
      }
   ],
   "nodes":[
      "String"
   ],
   "sets":[
      {
         "name":"String",
         "priority":"int",
         "names":[
            "String"
         ]
      }
   ]
}
```

### Possible Values for *name*:
* Every String except for these: PRG_ROOT, PATHS, PRG_END

### <a name="names-section"></a>Possible Values for *names*:
* All names of nodes, subpaths or sets
* _**Warning**_ Looping causes errors

### Possible Values for *capacity* (Subpath only):
* All integers (Length of list has to be the same as **[names](#names-section)** list)

### Possible Values for *probs* (Subpath only):
* All floats (Length of list has to be the same as **[names](#names-section)** list)

### Possible Values for *type* (Subpath only):
<table>
  <tr>
    <th>Integer</th>
    <th>Subpath Type</th>
  </tr>
  <tr>
    <td>0</td>
    <td>Normal</td>
  </tr>
  <tr>
    <td>1</td>
    <td>Random</td>
  </tr>
</table>

### Possible Values for *priority* (Set only):
<table>
  <tr>
    <th>Integer</th>
    <th>Set Priority Type</th>
  </tr>
  <tr>
    <td>0</td>
    <td>Closest</td>
  </tr>
  <tr>
    <td>1</td>
    <td>Furthest</td>
  </tr>
  <tr>
    <td>2</td>
    <td>Shortest Wait</td>
  </tr>
  <tr>
    <td>3</td>
    <td>Shortest & Closest Wait</td>
  </tr>
  <tr>
    <td>4</td>
    <td>Random</td>
  </tr>
  <tr>
    <td>5</td>
    <td>Random & Empty</td>
  </tr>
  <tr>
    <td>6</td>
    <td>Lowest Frequency</td>
  </tr>
</table>
