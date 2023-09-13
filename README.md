# Praktikum-2023

Idea: Convert a hirachical structure to a graph to visualize paths better.

## <a name="names-section"></a>Modules used
- Flatlaf
- Jgraphx
- Json

## Description
Can be used to visualize the hirarchical structure of paths in the software crowd:it by accu:rate (germany)

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

### Possible Values for **name**:
* Every String except for these: PRG_ROOT, PATHS, PRG_END

### Possible Values for **names**:

* All names of nodes, subpaths or sets
* _**Warning**_ Looping causes errors

### Possible Values for **capacity**:
* All integers (Has to has to have the same length as **[names](#names-section)**
