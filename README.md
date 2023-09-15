# <center>Praktikum-2023</center>

## Description
Can be used to visualize the hierarchical structure of paths in the software crowd:it by accu:rate (📍 Germany)

## Usage
To use this software, first clone the GitHub repo and navigate to out/artifacts/Praktikum2023_jar. You will find three files located there:
* A file named config.cfg, that includes project [config](#config)
* A folder named testdata . This folder contains some json files for testing and demonstration.
* A java archive named Praktikum2023.jar . You can run this file. If you want more debug information, use the cmd to execute the file.
  ```java -jar Praktikum2023.jar``` **WARNING: You need to have java version 8+ installed.**

## Config
The config.cfg file is used to configure the project.
<table>
    <tr>
        <th>key</th>
        <th>value</th>
        <th>default</th>
    </tr>
    <tr>
        <td>JSON</td>
        <td>Relative path to json</td>
        <td>testdata/demo.json</td>
    </tr>
    <tr>
        <td>START_COLLAPSED</td>
        <td>Boolean whether the graph is collapsed by default or not</td>
        <td>false</td>
    </tr>
  <tr>
        <td>USE_LAYOUT</td>
        <td>Boolean whether the graph is using the layout</td>
        <td>true</td>
    </tr>
</table>

**IMPORTANT: THE config.cfg file has to be kept in the same directory as the jar file at all times.**

## JSON
Configure the json with the following structure.

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
* All names of nodes, sub-paths or sets
* _**Warning**_ Looping causes errors

### Possible Values for *capacity* (Sub-path only):
* All integers (Length of list has to be the same as **[names](#names-section)** list)

### Possible Values for *probs* (Sub-path only):
* All floats (Length of list has to be the same as **[names](#names-section)** list)

### Possible Values for *type* (Sub-path only):
<table>
  <tr>
    <th>Integer</th>
    <th>Sub-path Type</th>
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

## Tools & Shortcuts

The following tools are available:
* Save as png (Ctrl + S)
* Save as svg (Ctrl + V)
* Collapse all (Ctrl + C)
* Expand all (Ctrl + E)

All tools are accessible under the tools menu